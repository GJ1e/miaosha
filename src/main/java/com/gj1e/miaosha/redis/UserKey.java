package com.gj1e.miaosha.redis;


/**
 * @Author GJ1e
 * @Create 2019/12/16
 * @Time 15:43
 */
public class UserKey extends BasePrefix {


    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getUserById = new UserKey("id");
    public static UserKey getUserByName = new UserKey("name");
}
