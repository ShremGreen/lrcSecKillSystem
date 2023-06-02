package com.lrc.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrc.seckill.VO.GoodsVO;
import com.lrc.seckill.VO.OrderDetailVO;
import com.lrc.seckill.VO.RespBeanEnum;
import com.lrc.seckill.exception.GlobalException;
import com.lrc.seckill.mapper.OrderMapper;
import com.lrc.seckill.pojo.Order;
import com.lrc.seckill.pojo.SeckillGoods;
import com.lrc.seckill.pojo.SeckillOrder;
import com.lrc.seckill.pojo.User;
import com.lrc.seckill.service.IGoodsService;
import com.lrc.seckill.service.IOrderService;
import com.lrc.seckill.service.ISeckillGoodsService;
import com.lrc.seckill.service.ISeckillOrderService;
import com.lrc.seckill.utils.MD5Util;
import com.lrc.seckill.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2023-05-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    @Override
    public Order secKill(User user, GoodsVO goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //查询商品
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        //减库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        //查询商品
        boolean result = seckillGoodsService.update(
                new UpdateWrapper<SeckillGoods>()
                        .setSql("stock_count = " + "stock_count-1")
                        .eq("goods_id", goods.getId())
                        .gt("stock_count", 0));
        //判断是否有库存
        if(seckillGoods.getStockCount() < 1) {
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);

        return order;
    }

    /***
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVO detail(Long orderId) {

        if(orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVO goodsVO = goodsService.findGoodsVOByGoodsId(order.getGoodsId());
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrder(order);
        orderDetailVO.setGoodsVO(goodsVO);
        return orderDetailVO;
    }

    /***
     * 创建秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String s = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, s, 60, TimeUnit.SECONDS);
        return s;
    }

    /***
     * 秒杀路径校验
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if(user == null || StringUtils.isEmpty(path)) return false;
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /***
     * 验证码校验
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (StringUtils.isEmpty(captcha) || null == user || goodsId < 0) return false;
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return redisCaptcha.equals(captcha);
    }
}
