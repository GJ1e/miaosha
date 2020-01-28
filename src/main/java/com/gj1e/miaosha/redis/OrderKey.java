package com.gj1e.miaosha.redis;

/**
 * @Author GJ1e
 * @Create 2020/1/28
 * @Time 21:55
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("MSOrder");
}
