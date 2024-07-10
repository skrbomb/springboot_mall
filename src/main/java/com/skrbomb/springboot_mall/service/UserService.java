package com.skrbomb.springboot_mall.service;

import com.skrbomb.springboot_mall.dto.UserRegisterRequest;
import com.skrbomb.springboot_mall.model.User;

public interface UserService {

     Integer register(UserRegisterRequest userRegisterRequest);

     User getUserById(Integer userId);
}
