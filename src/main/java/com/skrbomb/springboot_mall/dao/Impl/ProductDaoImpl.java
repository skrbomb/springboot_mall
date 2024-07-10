package com.skrbomb.springboot_mall.dao.Impl;

import com.skrbomb.springboot_mall.dao.ProductDao;
import com.skrbomb.springboot_mall.dto.ProductQueryParams;
import com.skrbomb.springboot_mall.dto.ProductRequest;
import com.skrbomb.springboot_mall.model.Product;
import com.skrbomb.springboot_mall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        //使用 sql中的count(*) 去查詢在 product TABLE 中符合條件的數據總數
        String sql=" SELECT count(*) FROM product WHERE 1=1";

        Map<String,Object> map=new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory()!=null){
            sql+=" AND category=:category";
            //toString()跟toName()都有轉換成字串的功能
            map.put("category",productQueryParams.getCategory().toString());
        }

        if(productQueryParams.getSearch()!=null){
            //sql拼接字串前要留一個空格以免sql語法錯誤
            sql+=" AND product_name LIKE :search";
            //將前端傳過來search的值，前後加上"%"，目的是為了達到模糊查詢的效果
            map.put("search","%"+productQueryParams.getSearch()+"%");
        }

        /*這個queryForObject 方法通常是用在取count值的時候
        * 第三個參數要填上Integer.class 表示將count值從資料庫的值轉換成Integer類型的對象*/
        Integer total=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);
        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        //使用WHERE 1=1, 方便後續追加查詢條件
        String sql="SELECT product_id , product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE 1=1";

        Map<String,Object> map=new HashMap<>();

        //查詢條件
        if(productQueryParams.getCategory()!=null){
            sql+=" AND category=:category";
            //toString()跟toName()都有轉換成字串的功能
            map.put("category",productQueryParams.getCategory().toString());
        }

        if(productQueryParams.getSearch()!=null){
            //sql拼接字串前要留一個空格以免sql語法錯誤
            sql+=" AND product_name LIKE :search";
            //將前端傳過來search的值，前後加上"%"，目的是為了達到模糊查詢的效果
            map.put("search","%"+productQueryParams.getSearch()+"%");
        }

        //排序
        //在實作這種 ORDER BY 的sql語法時，只能透過字串拼接的方式，而無法透過sql的變數實作
        //不用檢查null,因為在ProductController有設定defaultValue = "created_date"跟defaultValue = "desc"，預設透過創建日期來降序
        sql=sql+" ORDER BY "+productQueryParams.getOrderBy()+" "+productQueryParams.getSort();

        //分頁
        sql=sql+" LIMIT :limit OFFSET :offset";
        map.put("limit",productQueryParams.getLimit());
        map.put("offset",productQueryParams.getOffset());

        List<Product> productList =namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());

        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {

        String sql="SELECT product_id , product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id=:productId";

        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if(productList.size()>0){
            return productList.get(0);
        }else {
            return null;
        }


    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";
        Map<String,Object> map=new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now =new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder=new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),keyHolder);

        int productId=keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        //修改商品數據 要記得更新lastModifiedDate
        String sql="UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl, price=:price, stock=:stock, description=:description, last_modified_date=:lastModifiedDate WHERE product_id=:productId";

        Map<String,Object> map=new HashMap<>();
        map.put("productId",productId);

        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        //紀錄更新數據當下時間點
        map.put("lastModifiedDate",new Date());

        namedParameterJdbcTemplate.update(sql,map);

    }

    @Override
    public void deleteProductById(Integer productId) {

        String sql="DELETE FROM product WHERE product_id=:productId";

        Map<String, Object> map=new HashMap<>();
        map.put("productId",productId);
        namedParameterJdbcTemplate.update(sql,map);
    }
}
