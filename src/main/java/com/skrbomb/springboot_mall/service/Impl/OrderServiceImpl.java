package com.skrbomb.springboot_mall.service.Impl;

import com.skrbomb.springboot_mall.dao.OrderDao;
import com.skrbomb.springboot_mall.dao.ProductDao;
import com.skrbomb.springboot_mall.dto.BuyItem;
import com.skrbomb.springboot_mall.dto.CreateOrderRequest;
import com.skrbomb.springboot_mall.model.OrderItem;
import com.skrbomb.springboot_mall.model.Product;
import com.skrbomb.springboot_mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        int totalValue=0;
        List<OrderItem> orderItemList=new ArrayList<>();

        for(BuyItem buyItem: createOrderRequest.getBuyItemList()){
            Product product=productDao.getProductById(buyItem.getProductId());

            //計算總價
            int amount=buyItem.getQuantity()*product.getPrice();
            totalValue+=amount;

            //轉換BuyItem to OrderItem
            OrderItem orderItem=new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        /*order TABLE 儲存的是user_id , total_amount 等資訊
        * user_id=:userId  我們只需要計算total_amount 總金額*/
        Integer orderId=orderDao.createOrder(userId,totalValue);

        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
