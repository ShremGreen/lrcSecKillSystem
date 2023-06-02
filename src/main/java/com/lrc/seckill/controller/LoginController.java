package com.lrc.seckill.controller;

import com.lrc.seckill.VO.LoginVO;
import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private IUserService userService;

    /***
     * 跳转登陆页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /***
     * 登录功能
     * @param loginVO
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody//传参注解
    public RespBean doLogin(@Valid LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {

        return userService.dologin(loginVO, request, response);
    }
}
