package com.lrc.seckill.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//公用返回对象枚举
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERR0R(500,"服务端异常"),

    //登录
    LOGIN_ERROR(50001, "用户名或密码不正确"),
    MOBILE_ERROR(50002, "用户名格式不正确"),
    BIND_ERROR(50003, "参数校验异常"),
    MOBILE_NOT_EXIST(50004, "该用户名不存在"),
    PASSWORD_UPDATE_FAIL(50005, "更新密码失败"),
    //秒杀
    EMPTY_STOCK_ERROR(50011, "空库存异常"),
    REPEAT_ERROR(50012, "每人只能限购一件"),
    SESSION_ERROR(50013, "该用户不存在"),
    REQUEST_ILLEGAL(50014, "请求非法"),
    CAPTCHA_ERROR(50015, "验证码错误"),
    ACCESS_LIMIT_REACHED(50016, "访问过于频繁"),
    //订单
    ORDER_NOT_EXIST(50021, "该订单信息不存在");
    ;

    private final Integer code;
    private final String message;
}
