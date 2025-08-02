package com.example.ProductsService.service;

import com.example.ProductsService.Exception.ProductNotFoundException;
import com.example.ProductsService.dao.ProductRepo;
import com.example.ProductsService.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Product createProduct(Product product,HttpServletRequest request) throws AccessDeniedException {
        if(!userFeignClient.verifyUser(request))
        {
            throw new AccessDeniedException("User not authorized to access the resource");
        }
        productRepo.save(product);
       return product ;
    }
    public Product updateProduct(Product product, HttpServletRequest request) throws AccessDeniedException {

        if(!userFeignClient.verifyUser(request))
        {
            throw new AccessDeniedException("User not authorized to access the resource");
        }

       Product updatedProduct = getProductById(product.getId());
       if(updatedProduct==null)
       {
           throw new ProductNotFoundException("Product not found for id "+product.getId());
       }
       updatedProduct.setName(product.getName());
       updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setQuantity(product.getQuantity());

        productRepo.save(updatedProduct);
        return updatedProduct;
    }
    public void deleteProduct(int productId,HttpServletRequest request) throws AccessDeniedException {
        if(!userFeignClient.verifyUser(request))
        {
            throw new AccessDeniedException("User not authorized to access the resource");
        }
        Product product= getProductById(productId);
        if(product==null)
        {
            throw new ProductNotFoundException("Product not found for id "+productId);
        }
        productRepo.deleteById(Math.toIntExact(productId));
    }

    public List<Product> getAllProducts()
    {
        return productRepo.findAll();
    }

    public Product getProductById(int productId)
    {
        Product product= productRepo.findById(productId);
        if(product==null)
        {
            throw new ProductNotFoundException("Product not found for id "+productId);
        }
       return productRepo.findById(productId);
    }
    
}
