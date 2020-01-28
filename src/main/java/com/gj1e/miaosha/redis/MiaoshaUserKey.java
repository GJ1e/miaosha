package com.gj1e.miaosha.redis;


/**
 * @Author GJ1e
 * @Create 2019/12/16
 * @Time 15:43
 */

public class MiaoshaUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600*24*2;  //Token 有效期2天。

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"token");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0,"id");
}
