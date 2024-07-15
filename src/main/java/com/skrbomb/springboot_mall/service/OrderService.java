package com.skrbomb.springboot_mall.service;

import com.skrbomb.springboot_mall.dto.CreateOrderRequest;
import com.skrbomb.springboot_mall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

}
