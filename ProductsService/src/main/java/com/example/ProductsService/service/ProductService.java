package com.example.ProductsService.service;

import com.example.ProductsService.Exception.ProductNotFoundException;
import com.example.ProductsService.dao.ProductRepo;
import com.example.ProductsService.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserFeignClient userFeignClient;


    public Product createProduct(Product product, String request) throws AccessDeniedException {

        ResponseEntity<String> response=verifyUser(request);
        if(response.getStatusCode()== HttpStatus.OK)
        {
            productRepo.save(product);
        }


        return product;
    }

    public Product updateProduct(Product product, String request) throws AccessDeniedException {

        Product updatedProduct = getProductById(product.getId());
        if (updatedProduct == null) {
            throw new ProductNotFoundException("Product not found for id " + product.getId());
        }
        ResponseEntity<String> response=verifyUser(request);
        if(response.getStatusCode()== HttpStatus.OK)
        {

            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setQuantity(product.getQuantity());

            productRepo.save(updatedProduct);
        }

        return updatedProduct;
    }

    public Product deleteProduct(int productId, String request) throws AccessDeniedException {
        ResponseEntity<String> response=verifyUser(request);

        Product product = getProductById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found for id " + productId);
        }
        if(response.getStatusCode()== HttpStatus.OK)
        {
            productRepo.deleteById(Math.toIntExact(productId));
        }


        return product;
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int productId) {
        Product product = productRepo.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found for id " + productId);
        }
        return productRepo.findById(productId);
    }

    @CircuitBreaker(name="ProductsService", fallbackMethod="serviceUnavailableResponse")
    public ResponseEntity<String> verifyUser(String request) throws AccessDeniedException {

        if (!userFeignClient.verifyUser(request)) {
            throw new AccessDeniedException("User not authorized to access the resource");

        }
        return ResponseEntity.status(HttpStatus.OK).body("User verified");
    }

    public ResponseEntity<String> serviceUnavailableResponse(String request, Throwable throwable)
    {
        System.err.println("Fallback triggered due to: " + throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User service temporarily unavailable");
    }
    
}
