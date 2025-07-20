package com.example.ProductsService.dao;

import com.example.ProductsService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    Product findById(int id);
}
