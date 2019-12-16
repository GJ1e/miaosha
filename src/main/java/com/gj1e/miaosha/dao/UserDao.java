package com.gj1e.miaosha.dao;

import com.gj1e.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author GJ1e
 * @Create 2019/12/3
 * @Time 20:19
 */
@Repository
@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE id=#{id}")
     User getById(@Param("id")int id);

    @Insert("INSERT INTO user(id,name) VALUES(#{id},#{name})")
    int insertUser(User user);
}
