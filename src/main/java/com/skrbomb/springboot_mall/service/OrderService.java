package com.skrbomb.springboot_mall.service;

import com.skrbomb.springboot_mall.dto.CreateOrderRequest;

public interface OrderService {


    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
