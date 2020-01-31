package com.gj1e.miaosha.redis;

/**
 * @Author GJ1e
 * @Create 2020/1/31
 * @Time 16:58
 */
public class AccessKey extends BasePrefix {

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expire){
        return new AccessKey(expire,"access");
    }
}
