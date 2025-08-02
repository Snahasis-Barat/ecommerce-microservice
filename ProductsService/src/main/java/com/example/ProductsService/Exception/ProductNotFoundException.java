package com.example.ProductsService.Exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }
}
