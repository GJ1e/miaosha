package com.gj1e.miaosha.util;

import java.util.UUID;

/**
 * @Author GJ1e
 * @Create 2019/12/18
 * @Time 9:15
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
