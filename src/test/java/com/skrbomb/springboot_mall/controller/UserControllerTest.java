package com.skrbomb.springboot_mall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skrbomb.springboot_mall.dao.UserDao;
import com.skrbomb.springboot_mall.dto.UserLoginRequest;
import com.skrbomb.springboot_mall.dto.UserRegisterRequest;
import com.skrbomb.springboot_mall.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    private ObjectMapper objectMapper=new ObjectMapper();


    //註冊新帳號
    @Test
    public void register_success() throws Exception {
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("test1@gmail.com");
        userRegisterRequest.setPassword("123456");

        //writeValueAsString是objectMapper類的一個方法， 用於將Java對象轉為JSON格式資料(序列化Serialization)
        String json=objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder= MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId",notNullValue()))
                .andExpect(jsonPath("$.email",equalTo("test1@gmail.com")))
                .andExpect(jsonPath("$.createdDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate",notNullValue()));

        //檢查資料庫中的密碼不為明碼
        User user=userDao.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPassword(),user.getPassword());
    }

    @Test
    public void register_invalidEmailFormat() throws Exception {
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("dsfsafdsfasdf");
        userRegisterRequest.setPassword("123456");

        String json=objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("test2@gmail.com");
        userRegisterRequest.setPassword("123456");

        String json=objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //第一次註冊成功
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        //第二次重複註冊失敗
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    //登入
    @Test
    public void login_success() throws Exception {
        //先註冊一個新帳號
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("test3@gmail.com");
        userRegisterRequest.setPassword("123456");

        register(userRegisterRequest);

        //再測試登入功能
        UserLoginRequest userLoginRequest=new UserLoginRequest();
        userLoginRequest.setEmail("test3@gmail.com");
        userLoginRequest.setPassword("123456");

        String json=objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userId",notNullValue()))
                .andExpect(jsonPath("$.email",equalTo(userRegisterRequest.getEmail())))
                .andExpect(jsonPath("$.createdDate",notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate",notNullValue()));
    }

    @Test
    public void login_wrongPassword() throws Exception {
        //先註冊新帳號
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("test4@gmail.com");
        userRegisterRequest.setPassword("123456");
        register(userRegisterRequest);

        //登入帳號時輸入錯誤的密碼
        UserLoginRequest userLoginRequest=new UserLoginRequest();
        userLoginRequest.setEmail("test4@gmail.com");
        userLoginRequest.setPassword("wrongPassword");
        String json=objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_wrongEmail() throws Exception {
        UserRegisterRequest userRegisterRequest=new UserRegisterRequest();
        userRegisterRequest.setEmail("test5@gmail.com");
        userRegisterRequest.setPassword("123456");
        register(userRegisterRequest);

        UserLoginRequest userLoginRequest=new UserLoginRequest();
        userLoginRequest.setEmail("wrongEmail");
        userLoginRequest.setPassword("123456");
        String json=objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_invalidEmailFormat() throws Exception {
        UserLoginRequest userLoginRequest=new UserLoginRequest();
        userLoginRequest.setEmail("testail.com");
        userLoginRequest.setPassword("123456");

        String json=objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));

    }

    @Test
    public void login_emailNotExist() throws Exception {
        UserLoginRequest userLoginRequest=new UserLoginRequest();
        userLoginRequest.setEmail("unknown@gmail.com");
        userLoginRequest.setPassword("123456");

        String json=objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }



    private void register(UserRegisterRequest userRegisterRequest) throws Exception {
        String json=objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder=MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }





}