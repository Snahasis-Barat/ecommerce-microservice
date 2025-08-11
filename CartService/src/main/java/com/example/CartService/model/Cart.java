package com.example.CartService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
   private int id;

    private int userId;

    private int productId;

    private String productName;

    private String productDescription;

    private long productPrice;

    private int productQuantity;


}
