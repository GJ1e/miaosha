package com.gj1e.miaosha.access;

import com.gj1e.miaosha.domain.MiaoshaUser;

/**
 * @Author GJ1e
 * @Create 2020/1/31
 * @Time 17:45
 */
public class UserCotext {
    private static ThreadLocal<MiaoshaUser> userHold = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        userHold.set(user);
    }

    public static MiaoshaUser getUser(){
        return userHold.get();
    }
}
