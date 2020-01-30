package com.gj1e.miaosha.service;

import com.gj1e.miaosha.dao.GoodsDao;
import com.gj1e.miaosha.domain.Goods;
import com.gj1e.miaosha.domain.MiaoshaGoods;
import com.gj1e.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author GJ1e
 * @Create 2019/12/19
 * @Time 23:05
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        int ret = goodsDao.reduceStock(miaoshaGoods);
        return ret>0;
    }
}
