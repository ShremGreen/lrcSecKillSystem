package com.lrc.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrc.seckill.VO.LoginVO;
import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.VO.RespBeanEnum;
import com.lrc.seckill.config.RedisConfig;
import com.lrc.seckill.exception.GlobalException;
import com.lrc.seckill.mapper.UserMapper;
import com.lrc.seckill.pojo.User;
import com.lrc.seckill.service.IUserService;
import com.lrc.seckill.utils.CookieUtil;
import com.lrc.seckill.utils.MD5Util;
import com.lrc.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2023-05-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 登录功能
     * @param loginVO
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean dologin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        //校验 //已经用注解取代
/*        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)) {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/
        //数据库查询
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if(null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!user.getPassword().equals(MD5Util.storageMD5(password,user.getSalt()))) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        //request.getSession().setAttribute(ticket, user);
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }

    /***
     * 根据cookie获取用户
     * @param ticket
     * @return
     */
    @Override
    public User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(ticket)) return null;
        User user = (User) redisTemplate.opsForValue().get("user:" + ticket);
        if(user != null) {
            CookieUtil.setCookie(request, response, "userTicket", ticket);
        }
        return user;
    }

    /***
     * 更新密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.frontendToStorageMD5(password, user.getSalt()));
        int i = userMapper.updateById(user);
        if(i == 1) {
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        } else {
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }
    }
}
