package com.skrbomb.springboot_mall.controller;


import com.skrbomb.springboot_mall.constant.ProductCategory;
import com.skrbomb.springboot_mall.dto.ProductQueryParams;
import com.skrbomb.springboot_mall.dto.ProductRequest;
import com.skrbomb.springboot_mall.model.Product;
import com.skrbomb.springboot_mall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //url path代表的是每一個資源之間階層關係
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search
            ){
        /*將前端傳遞過來的參數統一的整理到productQueryParams 再把該變數放到getProducts()中傳遞
        *這樣做的好處是未來要添加新的查詢條件的時候 不必再去Service層和Dao層修改方法定義
        * 只要在ProductQueryParams這個class中去增加變數即可*/
        ProductQueryParams productQueryParams=new ProductQueryParams();
        //把前端傳過來的category,search的值 set 到 productQueryParams中
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        //回傳的是一個商品的列表
        List<Product> productList=productService.getProducts(productQueryParams);
/*        基於RESTFUL API的設計理念，查詢列表類型的api不管有無數據都要返回Status Code=200 OK
        而若是查詢單個數據的API的話，則是有查到才會回200 OK,沒查到則回應404 NOT_FOUND*/
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

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


