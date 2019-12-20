package com.gj1e.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author GJ1e
 * @Create 2019/12/19
 * @Time 22:53
 * 秒杀订单
 */
@Getter
@Setter
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
