package com.gj1e.miaosha.vo;

import com.gj1e.miaosha.domain.MiaoshaUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author GJ1e
 * @Create 2019/12/24
 * @Time 15:39
 */
@Getter
@Setter
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private MiaoshaUser user;
}
