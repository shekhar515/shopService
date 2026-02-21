package com.example.shopService.exception;

public class ShopNotFoundException extends RuntimeException{
public ShopNotFoundException(String message) {
        super(message);
    }
}
