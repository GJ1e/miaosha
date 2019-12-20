package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.service.GoodsService;
import com.gj1e.miaosha.service.MiaoshaUserService;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author GJ1e
 * @Create 2019/12/18
 * @Time 9:53
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;


    //登陆成功跳转商品列表，根据Token来判断用户。
    @RequestMapping("/to_list")
    public String toList(Model model,MiaoshaUser user){
        model.addAttribute("user", user);

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }


    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId){
        model.addAttribute("user", user);

        //
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        //获取秒杀的开始和结束时间
        Long startAt = goods.getStartDate().getTime();
        Long endAt = goods.getEndDate().getTime();
        Long now = System.currentTimeMillis();

        //查看秒杀状态
        int miaoshaStatus = 0;//秒杀状态
        int remainSeconds = 0; //秒杀剩余时间
        if (now < startAt){//秒杀没开始，
            miaoshaStatus = 0;
            remainSeconds= (int)(startAt-now);
        }else if (now > endAt){ //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }
}
