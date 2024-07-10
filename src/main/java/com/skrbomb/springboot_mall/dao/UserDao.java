package com.skrbomb.springboot_mall.dao;

import com.skrbomb.springboot_mall.dto.UserRegisterRequest;
import com.skrbomb.springboot_mall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
