package com.gj1e.miaosha.redis;

/**
 * @Author GJ1e
 * @Create 2020/1/30
 * @Time 16:43
 */
public class MiaoshaKey extends BasePrefix {

    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");
}
