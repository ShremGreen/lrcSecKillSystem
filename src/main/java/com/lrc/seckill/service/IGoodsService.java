package com.lrc.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrc.seckill.VO.GoodsVO;
import com.lrc.seckill.pojo.Goods;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-05-24
 */
public interface IGoodsService extends IService<Goods> {
    /***
     * 返回商品列表
     * @return
     */
    List<GoodsVO> findGoodsVO();

    /***
     * 获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVO findGoodsVOByGoodsId(Long goodsId);
}
