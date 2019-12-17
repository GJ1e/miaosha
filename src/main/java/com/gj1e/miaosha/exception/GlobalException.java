package com.gj1e.miaosha.exception;

import com.gj1e.miaosha.result.CodeMsg;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 18:11
 */
public class GlobalException extends RuntimeException {
    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
