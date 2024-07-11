package com.skrbomb.springboot_mall.service.Impl;

import com.skrbomb.springboot_mall.dao.UserDao;
import com.skrbomb.springboot_mall.dto.UserLoginRequest;
import com.skrbomb.springboot_mall.dto.UserRegisterRequest;
import com.skrbomb.springboot_mall.model.User;
import com.skrbomb.springboot_mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    //固定寫法 括號中填入該class名稱 用來創建出一個log變數
    private final static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查email有無被註冊過
        User user=userDao.getUserByEmail(userRegisterRequest.getEmail());

        //若user不為null ,則噴出一個exception終止前端這次的請求,並且指定HttpStatusCode=400 BAD REQUEST
        if(user!=null){
            log.warn("該email:{} 已經被註冊",userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //註冊新帳號時使用MD5 生成密碼的雜湊值，然後在userRegisterRequest中替換掉原密碼
        String hashedPassword= DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user=userDao.getUserByEmail(userLoginRequest.getEmail());
        //檢查User 是否存在
        if(user==null){
            log.warn("該email {}尚未被註冊",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //將User登入帳號時輸入的密碼使用MD5 生成密碼的雜湊值，再去與在資料庫中儲存的雜湊密碼做比較
        String hashedPassword=DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //比較密碼
        if(user.getPassword().equals(hashedPassword)){
            log.info("{} 登入成功",userLoginRequest.getEmail());
            return user;
        }else{
            log.warn("該email {} 的密碼錯誤",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
