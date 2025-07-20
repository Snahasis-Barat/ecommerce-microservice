package com.example.ProductsService.service;

import com.example.ProductsService.dao.ProductRepo;
import com.example.ProductsService.model.Product;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ProductService {


    @Autowired
    ProductRepo productRepo;

    public Product createProduct(Product product)
    {
        productRepo.save(product);
       return product ;
    }
    public Product updateProduct(int productId, Product product)
    {
       Product updatedProduct = getProductById(productId);
       updatedProduct.setName(product.getName());
       updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setQuantity(product.getQuantity());

        productRepo.save(updatedProduct);
        return updatedProduct;
    }
    public void deleteProduct(Long productId)
    {

    }
    public Product getProductById(int productId)
    {
        Product product= productRepo.findById(productId);
        if(product==null)
        {
            throw new NotFoundException("Product not found");
        }
       return productRepo.findById(productId);
    }
    
}
