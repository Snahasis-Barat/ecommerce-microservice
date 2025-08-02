package com.example.ProductsService.controller;


import com.example.ProductsService.model.Product;
import com.example.ProductsService.service.ProductService;
import com.example.ProductsService.service.UserFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;



   public ResponseEntity<?> addProducts(@RequestBody Product product,HttpServletRequest request)
   {
       try{
           return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product,request));
       }
       catch(Exception e)
       {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
       }
   }

   public ResponseEntity<?> updateProduct(@RequestBody Product product, HttpServletRequest request)
   {
      try{
         return  ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(product,request));
      }
      catch(Exception e)
      {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
      }
   }

    public ResponseEntity<?> getProducts()
    {

            return  ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());


    }


}
