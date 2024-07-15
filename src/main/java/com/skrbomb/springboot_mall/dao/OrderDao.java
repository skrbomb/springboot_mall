package com.skrbomb.springboot_mall.dao;

import com.skrbomb.springboot_mall.dto.OrderQueryParams;
import com.skrbomb.springboot_mall.model.Order;
import com.skrbomb.springboot_mall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId,Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemByOrderId(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);
}
