package com.gj1e.miaosha.result;

import lombok.Getter;

/**
 * @Author GJ1e
 * @Create 2019/12/3
 * @Time 17:40
 *
 * Result消息结果封装
 */
@Getter
public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
