package com.example.CartService.dto;

import java.util.List;

public class AddToCartRequest {

    private List<CartRequest> items;

    public List<CartRequest> getItems() {
        return items;
    }

    public void setItems(List<CartRequest> items) {
        this.items = items;
    }
}
