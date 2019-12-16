package com.gj1e.miaosha.service;

import com.gj1e.miaosha.dao.UserDao;
import com.gj1e.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author GJ1e
 * @Create 2019/12/3
 * @Time 20:24
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public Boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("jjj");
        userDao.insertUser(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("aaa");
        userDao.insertUser(u2);

        return true;

    }
}
