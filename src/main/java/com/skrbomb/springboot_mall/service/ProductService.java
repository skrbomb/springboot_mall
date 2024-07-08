package com.skrbomb.springboot_mall.service;

import com.skrbomb.springboot_mall.dto.ProductRequest;
import com.skrbomb.springboot_mall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

}
