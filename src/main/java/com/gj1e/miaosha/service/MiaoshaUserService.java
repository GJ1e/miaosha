package com.gj1e.miaosha.service;

import com.gj1e.miaosha.dao.MiaoshaUserDao;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.exception.GlobalException;
import com.gj1e.miaosha.redis.MiaoshaUserKey;
import com.gj1e.miaosha.redis.RedisService;
import com.gj1e.miaosha.result.CodeMsg;
import com.gj1e.miaosha.util.MD5Util;
import com.gj1e.miaosha.util.UUIDUtil;
import com.gj1e.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 16:10
 */
@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public MiaoshaUser getById(Long id){
        //取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (miaoshaUser!=null){
            return miaoshaUser;
        }
        //取数据库
        miaoshaUser = miaoshaUserDao.getById(id);
        if (miaoshaUser!=null){
            redisService.set(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        }
        return miaoshaUser;
    }

    //修改密码
    public Boolean updatePassword(String token, Long id,String passwordNew){
        //取User
        MiaoshaUser miaoshaUser = getById(id);
        if (miaoshaUser==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,miaoshaUser.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        //更新缓存
        redisService.del(MiaoshaUserKey.getById,""+id);
        miaoshaUser.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);

        return true;
    }
    //从Redis中获取token
    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);

        //延长有效期   若用户在有效期内重新登陆，则把Cookie从新写入redis，延长Token的有效期
        if (user != null){
            addCookie(response,token,user);
        }
        return user;
    }




    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String fromPass = loginVo.getPassword();

        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword(); //获取数据库中的密码
        String saltDB = user.getSalt(); //获取数据库中的盐
        String calcPass = MD5Util.formPassToDBPass(fromPass,saltDB);
        if (!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //登陆成功
        String token = UUIDUtil.uuid();     //生成token
        addCookie(response,token,user);     //对token执行相应的操作
        return true;
    }


    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {

        redisService.set(MiaoshaUserKey.token,token,user);  //将Token存到Redis中
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds()); //token的过期时间和Redis中的过期时间一致
        cookie.setPath("/");
        response.addCookie(cookie); //将Cookie放到客户端。
    }
}
