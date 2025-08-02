package com.example.ProductsService.service;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8080",name="UserService")
public interface UserFeignClient {

    @GetMapping("/verifyUser")
    boolean verifyUser(HttpServletRequest request);
}
