package com.lrc.seckill.exception;
//全局异常

import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.VO.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}
