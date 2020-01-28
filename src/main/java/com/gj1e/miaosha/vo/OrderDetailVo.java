package com.gj1e.miaosha.vo;

import com.gj1e.miaosha.domain.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author GJ1e
 * @Create 2019/12/24
 * @Time 20:29
 */
@Getter
@Setter
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
