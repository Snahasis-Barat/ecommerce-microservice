package com.example.CartService.Service;

import com.example.CartService.dao.CartRepository;
import com.example.CartService.dto.AddToCartRequest;
import com.example.CartService.dto.CartRequest;
import com.example.CartService.dto.CartResponse;
import com.example.CartService.model.Cart;
import com.example.ProductsService.model.Product;
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
    CartRepository cartRepository;

    @Autowired
    UserFeignClient userFeignClient;


    @Autowired
    ProductFeignClient productFeignClient;

    public List<Cart> addToCart(String headerRequest, List<CartRequest> cartRequest) throws AccessDeniedException {

        String username = getUserName(headerRequest);
//        List<Integer> productIds = cartRequest.stream()
//                .map(CartRequest::getProductId)
//                .toList();

        List<Cart> cartItems=cartRequest.stream().map(items-> {


            Product p=productFeignClient.fetchProductById(items.getProductId());
            return Cart.builder()
                    .userName(username)
                    .productId(p.getId())
                    .productName(p.getName())
                    .productDescription(p.getDescription())
                    .productPrice(p.getPrice())
                    .productQuantity(items.getQuantity())
                    .build();

        }).collect(Collectors.toList());

        return cartRepository.saveAll(cartItems);
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User service temporarily unavailable");
    }
}
