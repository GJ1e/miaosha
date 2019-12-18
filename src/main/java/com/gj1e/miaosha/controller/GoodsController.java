package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //登陆成功跳转商品列表，根据Token来判断用户。
    @RequestMapping("/to_list")
    public String toList(Model model,MiaoshaUser user){
        model.addAttribute("user", user);
        return "goods_list";
    }
}
