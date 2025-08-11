package com.example.CartService.Controller;


import com.example.CartService.Service.CartService;
import com.example.CartService.dto.AddToCartRequest;
import com.example.CartService.dto.CartRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.CacheResponse;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String headerRequest, @RequestBody AddToCartRequest cartRequest)
    {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(headerRequest,cartRequest));
        }

        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
