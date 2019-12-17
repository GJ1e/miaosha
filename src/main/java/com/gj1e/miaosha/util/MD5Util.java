package com.gj1e.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 10:30
 *
 */
public class MD5Util {

    //生成MD5校验字符串
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }


    private static final String salt = "1a2b3c4d";

    //用户端 Pass = MD5(明文+固定salt)
    public static String inputPassToFormPass(String inputPass){
        String formPass = ""+salt.charAt(1)+salt.charAt(3)+inputPass+salt.charAt(0);
        return md5(formPass);
    }

    //服务端 Pass = MD5(用户输入+随机salt)
    public static String formPassToDBPass(String formPass,String salt){
        String dbPass = ""+salt.charAt(1)+salt.charAt(3)+formPass+salt.charAt(0);
        return md5(dbPass);
    }

    public static String inputPasssToDBPass(String inputPass, String saltDB){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {    //5f1e93689cca76d818d1df7994a7bd0c
        System.out.println(inputPassToFormPass("123456"));  //07fcadd3e27b63feda1681772d4844b3
        System.out.println(formPassToDBPass("123456","1a2b3c4d"));
        System.out.println(inputPasssToDBPass("123456","1a2b3c4d"));
//        System.out.println(inputPasssToDBPass("123456","1a2b3c4d"));//5f1e93689cca76d818d1df7994a7bd0c
    }

}
