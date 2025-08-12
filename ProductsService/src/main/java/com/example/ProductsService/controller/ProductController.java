package com.example.ProductsService.controller;


import com.example.ProductsService.Exception.ProductNotFoundException;
import com.example.ProductsService.model.Product;
import com.example.ProductsService.service.ProductService;
import com.example.ProductsService.service.UserFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;


@PostMapping("/addProducts")
   public ResponseEntity<?> addProducts(@RequestBody Product product,@RequestHeader("Authorization") String request)
   {
       try{
           System.out.println("Authorisation header from Product service "+request);
           return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product,request));
       }
       catch(Exception e)
       {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }
   }

   @PutMapping("/updateProducts")
   public ResponseEntity<?> updateProduct(@RequestBody Product product, @RequestHeader("Authorization") String request)
   {
      try{
         return  ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(product,request));
      }
      catch(Exception e)
      {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
      }
   }
   @GetMapping("/getProducts")
    public ResponseEntity<?> getProducts()
    {

            return  ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());


    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(productId));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productService.getProductById(productId));
        }
    }

    @GetMapping("/fetchProduct/{id}")
    public Product fetchProductById(int productId) {
        try {
            return productService.getProductById(productId);
        } catch (ProductNotFoundException e) {
            return productService.getProductById(productId);
        }

    }


    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String request,@PathVariable int id)
    {

        try{
            return  ResponseEntity.status(HttpStatus.OK).body(productService.deleteProduct(id,request));
        }

        catch(ProductNotFoundException e)
        {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }


    }




}
