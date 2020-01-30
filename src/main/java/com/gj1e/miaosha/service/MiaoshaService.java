package com.gj1e.miaosha.service;

import com.gj1e.miaosha.domain.Goods;
import com.gj1e.miaosha.domain.MiaoshaOrder;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.domain.OrderInfo;
import com.gj1e.miaosha.redis.MiaoshaKey;
import com.gj1e.miaosha.redis.RedisService;
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
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }
}
