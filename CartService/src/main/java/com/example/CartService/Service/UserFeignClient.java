package com.example.CartService.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8080",name="UserService")
public interface UserFeignClient {

    @GetMapping("/users/verifyUser")
    boolean verifyUser(@RequestHeader("Authorization")String request);
}