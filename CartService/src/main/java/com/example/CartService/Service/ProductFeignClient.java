package com.example.CartService.Service;

import com.example.ProductsService.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8081",name="ProductsService")
public interface ProductFeignClient {

    @GetMapping("products/fetchProduct/{productId}")
    public Product fetchProductById(@PathVariable  int productId);


}
