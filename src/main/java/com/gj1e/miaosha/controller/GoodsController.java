package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.redis.GoodsKey;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.result.Result;
import com.gj1e.miaosha.service.GoodsService;
import com.gj1e.miaosha.service.MiaoshaUserService;
import com.gj1e.miaosha.vo.GoodsDetailVo;
import com.gj1e.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author GJ1e
 * @Create 2019/12/18
 * @Time 9:53
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;    //Thymeleaf手动渲染模板工具类

    @Autowired
    ApplicationContext applicationContext;


    //登陆成功跳转商品列表，根据Token来判断用户。
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response,
                         Model model, MiaoshaUser user) {
        model.addAttribute("user", user);

        //第一步从缓存中取出
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {//缓存中不为空，直接返回。
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        //return "goods_list";

        //为空手动渲染
        SpringWebContext cxt = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", cxt);
        if (!StringUtils.isEmpty(html)) {//不为空，就写到缓存中
            redisService.set(GoodsKey.getGoodsList, "", html);

        }
        return html;
    }


    @RequestMapping(value = "/to_detai2/{goodsId}")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, Model model,
                         MiaoshaUser user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {//缓存中不为空，直接返回。
            return html;
        }


        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //获取秒杀的开始和结束时间
        Long startAt = goods.getStartDate().getTime();
        Long endAt = goods.getEndDate().getTime();
        Long now = System.currentTimeMillis();

        //查看秒杀状态
        int miaoshaStatus = 0;//秒杀状态
        int remainSeconds = 0; //秒杀剩余时间
        if (now < startAt) {//秒杀没开始，
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - now);
        } else if (now > endAt) { //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        //手动渲染
        SpringWebContext cxt = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", cxt);
        if (!StringUtils.isEmpty(html)) {//不为空，就写到缓存中
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);

        }

        return html;
    }



    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
