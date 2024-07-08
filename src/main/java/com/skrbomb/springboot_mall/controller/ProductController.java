package com.skrbomb.springboot_mall.controller;


import com.skrbomb.springboot_mall.dto.ProductRequest;
import com.skrbomb.springboot_mall.model.Product;
import com.skrbomb.springboot_mall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId){
        Product product= productService.getProductById(productId);

        if(product!=null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }


    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId=productService.createProduct(productRequest);
        Product product=productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
/*    在ProductRequest中使用了@NotNull去驗證前端傳來的參數
    因此必須要在參數前面加上@Valid ,ProductRequest中的@NotNull才會真的生效*/

}


