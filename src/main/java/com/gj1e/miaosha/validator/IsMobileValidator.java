package com.gj1e.miaosha.validator;

import com.gj1e.miaosha.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 16:33
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    //初始化方法，拿到这个注解然后判断号码是否为空
    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    //判断注释中的值合不合法
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required){
            return ValidatorUtil.isMobile(s);
        }else {
            if (StringUtils.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtil.isMobile(s);   //判断号码格式是否合法
            }
        }
    }
}
