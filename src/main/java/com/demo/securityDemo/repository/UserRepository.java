package com.demo.securityDemo.repository;

import com.demo.securityDemo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRepository {

    @Insert("insert into user values ( null, #{userName}, #{userPassword}, #{userEmail}, #{role}, default )")
    int insertUser(User user);

    @Select("select * from user where user_name = #{username}")
    User findByUserName(String username);

}
