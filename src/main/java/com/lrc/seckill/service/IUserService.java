package com.lrc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrc.seckill.VO.LoginVO;
import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-05-19
 */
public interface IUserService extends IService<User> {
    /***
     * 登录抽象方法
     * @param loginVO
     * @param request
     * @param response
     * @return
     */
    RespBean dologin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);

    /***
     * 根据用户cookie获取用户
     * @param ticket
     * @return
     */
    User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response);

    /***
     * 更新密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
