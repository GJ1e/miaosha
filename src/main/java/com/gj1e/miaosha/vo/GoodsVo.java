package com.gj1e.miaosha.vo;

import com.gj1e.miaosha.domain.Goods;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author GJ1e
 * @Create 2019/12/19
 * @Time 23:08
 */
@Setter
@Getter
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
