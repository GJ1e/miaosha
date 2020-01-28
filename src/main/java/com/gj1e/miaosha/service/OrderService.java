package com.gj1e.miaosha.service;

import com.gj1e.miaosha.dao.OrderDao;
import com.gj1e.miaosha.domain.MiaoshaOrder;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.domain.OrderInfo;
import com.gj1e.miaosha.redis.OrderKey;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author GJ1e
 * @Create 2019/12/20
 * @Time 19:31
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);

        //从缓存中获取秒杀订单
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class);

    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    //生成订单
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);    //收货地址
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());//商品ID
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());//秒杀价格
        orderInfo.setOrderChannel(1); //下单渠道
        orderInfo.setStatus(0);//订单状态
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);//写入普通订单

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);//写入秒杀订单

        //写入订单到缓存
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goodsVo.getId(),miaoshaOrder);

        return orderInfo;
    }


}
