package com.lrc.seckill.VO;

import com.lrc.seckill.utils.ValidatorUtil;
import com.lrc.seckill.validator.IsMobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//手机号码校验规则
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    /***
     * 初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();//获取是否必填
    }

    /***
     * 是否满足格式校验
     * @param value
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //是否必填逻辑
        if(required) {//必填
            return ValidatorUtil.isMobile(value);
        } else {//非必填
            if(StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
