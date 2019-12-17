package com.gj1e.miaosha.vo;

import com.gj1e.miaosha.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 14:08
 */
@Getter
@Setter
@ToString
public class LoginVo {

    @NotNull
    @IsMobile   //自定义校验器
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

}
