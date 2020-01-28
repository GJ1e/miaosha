package com.gj1e.miaosha.redis;

/**
 * @Author GJ1e
 * @Create 2019/12/24
 * @Time 10:04
 */
public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl"); //页面缓存60秒
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd"); //页面缓存60秒
}
