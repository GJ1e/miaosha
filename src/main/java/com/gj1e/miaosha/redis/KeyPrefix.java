package com.gj1e.miaosha.redis;

/**
 * @Author GJ1e
 * @Create 2019/12/16
 * @Time 15:05
 *
 * 区分各个模块的Key
 */
public interface KeyPrefix {

    int expireSeconds(); //过期时间

    String getPrefix();

}
