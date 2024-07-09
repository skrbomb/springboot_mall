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
            //build()用來創建並返回一個ResponseEntity對象
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

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @Valid @RequestBody ProductRequest productRequest){
        //先檢查商品是否存在，再進行更新，若不存在則回傳404NOT FOUND
        Product product=productService.getProductById(productId);
        if(product!=null){
            //進行更新數據操作(沒有返回值)
            productService.updateProduct(productId,productRequest);
            //對更新過後的數據重新查詢並且返回給使用者
            Product updatedProduct=productService.getProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        Product product=productService.getProductById(productId);
        if(product!=null){
            productService.deleteProductById(productId);
            //Status code=204 NO_CONTENT代表請求已成功處理 但沒有內容返回
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


