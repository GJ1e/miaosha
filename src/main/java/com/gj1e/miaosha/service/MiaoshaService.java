package com.gj1e.miaosha.service;

import com.gj1e.miaosha.domain.Goods;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.domain.OrderInfo;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减少库存 下订单 写入 秒杀订单
        goodsService.reduceStock(goodsVo);

        return orderService.createOrder(user,goodsVo);
    }
}
