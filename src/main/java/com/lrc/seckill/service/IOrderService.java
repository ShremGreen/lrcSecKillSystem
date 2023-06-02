package com.lrc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrc.seckill.VO.GoodsVO;
import com.lrc.seckill.VO.OrderDetailVO;
import com.lrc.seckill.pojo.Order;
import com.lrc.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-05-24
 */
public interface IOrderService extends IService<Order> {

    /***
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order secKill(User user, GoodsVO goods);

    /***
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVO detail(Long orderId);

    /***
     * 创建秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /***
     * 秒杀路径校验
     * @param user
     * @param goodsId
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /***
     * 验证码校验
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
