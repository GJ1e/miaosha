package com.gj1e.miaosha.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author GJ1e
 * @Create 2019/12/19
 * @Time 22:49
 *
 * 商品表
 */
@Getter
@Setter
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
