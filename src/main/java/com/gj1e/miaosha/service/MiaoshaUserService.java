package com.gj1e.miaosha.service;

import com.gj1e.miaosha.dao.MiaoshaUserDao;
import com.gj1e.miaosha.domain.MiaoshaUser;
import com.gj1e.miaosha.exception.GlobalException;
import com.gj1e.miaosha.result.CodeMsg;
import com.gj1e.miaosha.util.MD5Util;
import com.gj1e.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author GJ1e
 * @Create 2019/12/17
 * @Time 16:10
 */
@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(Long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
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
        return true;
    }
}
