package com.example.ProductsService.dao;

import com.example.ProductsService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    Product findById(int id);
}
