package com.skrbomb.springboot_mall.dao;

import com.skrbomb.springboot_mall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
