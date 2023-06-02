package com.lrc.seckill.VO;

import com.lrc.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//商品详情页面返回对象
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVO {
    private User user;

    private GoodsVO goodsVO;

    private int secKillStatus;

    private int remainSeconds;
}
