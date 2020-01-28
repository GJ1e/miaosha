package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.domain.MiaoshaOrder;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.domain.OrderInfo;
import com.gj1e.miaosha.result.CodeMsg;
import com.gj1e.miaosha.result.Result;
import com.gj1e.miaosha.service.GoodsService;
import com.gj1e.miaosha.service.MiaoshaService;
import com.gj1e.miaosha.service.OrderService;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Author GJ1e
 * @Create 2019/12/20
 * @Time 18:50
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user,
                       @RequestParam("goodsId") long goodsId){
        model.addAttribute("user",user);
        //判断用户是否登录
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //判断是否秒杀到了，从订单表里判断
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (miaoshaOrder != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存，下订单，写入秒杀订单(三步操作必须是原子性的)
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
        return Result.success(orderInfo);
    }
}
