package com.lrc.seckill.exception;

import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.VO.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//全局异常处理类
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        if(e instanceof GlobalException) {
            GlobalException globalException = (GlobalException) e;
            return RespBean.error(globalException.getRespBeanEnum());
        } else if(e instanceof BindException) {
            BindException bindException = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage(respBean.getMessage() + "：" + bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        } else {
            return RespBean.error(RespBeanEnum.ERR0R);
        }
    }
}
