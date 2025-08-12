package com.example.CartService.Service;

import com.example.CartService.dto.AddToCartRequest;
import com.example.CartService.dto.CartRequest;
import com.example.CartService.dto.CartResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartService cartService;

    @Autowired
    UserFeignClient userFeignClient;

    public List<CartResponse> addToCart(String headerRequest, AddToCartRequest cartRequest) throws AccessDeniedException {


        if(verifyUser(headerRequest)==null)
        {
            throw new AccessDeniedException("User not authenticated");

        }
        List<CartResponse> cartItems=cartRequest.getItems().stream().map(items-> {

            CartResponse cartResponse=new CartResponse();
            cartResponse.setUserName(getUserName(headerRequest));
            
            return cartResponse;
        }).collect(Collectors.toList());

        return cartItems;
    }


    @CircuitBreaker(name="CartService", fallbackMethod="serviceUnavailableResponse")
    public ResponseEntity<String> verifyUser(String request) throws AccessDeniedException {

        if (!userFeignClient.verifyUser(request)) {
            throw new AccessDeniedException("User not authorized to access the resource");

        }
        return ResponseEntity.status(HttpStatus.OK).body("User verified");
    }

    @CircuitBreaker(name="CartService", fallbackMethod="serviceUnavailableResponse")
    public String getUserName(String request)  {

        return userFeignClient.getUserName(request);
    }

    public ResponseEntity<String> serviceUnavailableResponse(String request, Throwable throwable)
    {
        System.err.println("Fallback triggered due to: " + throwable.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User service temporarily unavailable");
    }
}
