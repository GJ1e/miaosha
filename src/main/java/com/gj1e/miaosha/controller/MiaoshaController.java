package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.access.AccessLimit;
import com.gj1e.miaosha.domain.MiaoshaOrder;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.rabbitmq.MQSender;
import com.gj1e.miaosha.rabbitmq.MiaoshaMessage;
import com.gj1e.miaosha.redis.AccessKey;
import com.gj1e.miaosha.redis.GoodsKey;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.result.CodeMsg;
import com.gj1e.miaosha.result.Result;
import com.gj1e.miaosha.service.GoodsService;
import com.gj1e.miaosha.service.MiaoshaService;
import com.gj1e.miaosha.service.OrderService;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author GJ1e
 * @Create 2019/12/20
 * @Time 18:50
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 1.系统初始化，加载商品库存到Redis
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }

        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * 优化思路：
     * 1.系统初始化，将商品库存预先加载到Redis中。
     * 2.收到请求，Redis预减库存，库存足够则进入第三步，否则返回失败。
     * 3.请求入队列，立即返回排队中。
     * 4.请求出队，生成订单，减少库存。
     * 5.客户端轮询，是否秒杀成功
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path) {
        model.addAttribute("user", user);
        //判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记,减少Redis的访问次数
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //判断是否秒杀到了，从订单表里判断
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        return Result.success(0);   //排队中
    }

    /**
     * 客户端轮询
     * 成功：OrderId
     * 排队中：0
     * 秒杀失败：-1
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        long result = miaoshaService.getMiaoshaOrderByUIdGId(user.getId(), goodsId);
        return Result.success(result);
    }

    /**
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(MiaoshaUser user,
                          @RequestParam("goodsId") long goodsId,
                          @RequestParam("verifyCode")int verifyCode) {
        //判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //检查验证码
        boolean check = miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    /**
     * 生成验证码的接口
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user,
                                               @RequestParam("goodsId") long goodsId) {
        //判断用户是否登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        BufferedImage image = miaoshaService.createVerifyCode(user,goodsId);
        try{
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

    }
}