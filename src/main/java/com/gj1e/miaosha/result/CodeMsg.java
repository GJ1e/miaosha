package com.gj1e.miaosha.result;

import lombok.Getter;
import lombok.ToString;

/**
 * @Author GJ1e
 * @Create 2019/12/3
 * @Time 17:40
 *
 * Result消息结果封装
 */
@Getter
@ToString
public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");

    //登录模块 5002XX
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号码不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"用户密码错误");

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

}
