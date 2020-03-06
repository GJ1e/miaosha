package com.gj1e.miaosha.service;

import com.gj1e.miaosha.domain.MiaoshaOrder;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.domain.OrderInfo;
import com.gj1e.miaosha.redis.MiaoshaKey;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.util.MD5Util;
import com.gj1e.miaosha.util.UUIDUtil;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author GJ1e
 * @Create 2019/12/20
 * @Time 19:28
 */
@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;


    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减少库存 下订单 写入 秒杀订单
        boolean success = goodsService.reduceStock(goodsVo);
        if (success){
            return orderService.createOrder(user,goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    /**
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public long getMiaoshaOrderByUIdGId(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        if (order != null){ //秒杀成功
            return order.getOrderId();
        }else { //判断秒杀失败的原因
            boolean isOver = getGoodsOver(goodsId); //商品是否售完
            if (isOver){
                return -1;  //秒杀失败
            }else {
                return 0;   //请求未处理完成，继续轮询。
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }

    /**
     * 检查Path
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if (user==null || path==null){
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,String.class);
        return path.equals(pathOld);

    }

    /**
     * 生成Path，作用于隐藏秒杀接口
     * @param user
     * @param goodsId
     * @return
     */
    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        if (user==null || goodsId<=0){
            return null;
        }

        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,str);
        return str;
    }

    /**
     * 生成验证码图片
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if (user==null || goodsId<=0){
            return null;
        }
        //定义图片宽度和高度
        int width = 80;
        int height = 32;

        //创建图片
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 设置背景颜色，并填充
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        // 把画笔“g”设置成黑色，画了一个矩形框
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // 在图片上生成50个干扰点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));   //设置颜色
        g.setFont(new Font("Candara", Font.BOLD, 24));  //设置字体
        g.drawString(verifyCode, 8, 24);    //把验证码写在图片上
        g.dispose();    //消掉画笔

        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;

    }

    //检查图片验证码
    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if (user==null || goodsId<=0){
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if (codeOld==null || codeOld-verifyCode != 0){
            return false;
        }
        redisService.del(MiaoshaKey.getMiaoshaVerifyCode,user.getId()+","+goodsId);
        return  true;
    }


    //计算验证码
    private int calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    //运算符数组
    private static char[] ops = new char[]{'+','-','*'};

    /**
     * 将数字与运算符随机排列
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }


}
