package com.gj1e.miaosha.rabbitmq;

import com.gj1e.miaosha.domain.MiaoshaUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author GJ1e
 * @Create 2020/1/30
 * @Time 15:33
 */
@Getter
@Setter
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
}
