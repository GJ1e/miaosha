package com.gj1e.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author GJ1e
 * @Create 2019/12/19
 * @Time 22:51
 * 秒杀商品表
 */
@Getter
@Setter
public class MiaoshaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
