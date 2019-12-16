package com.gj1e.miaosha.result;

import lombok.Getter;

/**
 * @Author GJ1e
 * @Create 2019/12/3
 * @Time 17:23
 */
@Getter
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    /**
     * 成功时候调用
     *
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败的时候调用
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<T>(cm);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if (cm == null)
            return;
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

}
