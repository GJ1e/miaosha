package com.gj1e.miaosha.controller;

import com.gj1e.miaosha.domain.User;
import com.gj1e.miaosha.rabbitmq.MQSender;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.redis.UserKey;
import com.gj1e.miaosha.result.CodeMsg;
import com.gj1e.miaosha.result.Result;
import com.gj1e.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author GJ1e
 * @Create 2019/12/2
 * @Time 20:18
 */

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","GJ1e");

        return "hello";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello world");
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq(){
//        sender.send("Hello GJ1e");
//        return Result.success("hello world");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic(){
//        sender.sendTopic("Hello GJ1e");
//        return Result.success("hello world");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout(){
//        sender.sendFanout("Hello GJ1e");
//        return Result.success("hello world");
//    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(){
        System.out.println("执行了");
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/dbGet")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/dbTX")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redisSet")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("GJ1e");
        redisService.set(UserKey.getUserById,""+1,user);
        return Result.success(true);
    }
    @RequestMapping("/redisGet")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getUserById,""+1,User.class);
        return Result.success(user);
    }

}
