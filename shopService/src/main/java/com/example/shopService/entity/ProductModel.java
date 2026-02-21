package com.example.shopService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;;
@Entity
@Table(name="products")
public class ProductModel {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String productName;
private Double price;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="shop_id")
@JsonIgnore
private ShopModel shop;
public ShopModel getShop() {
    return shop;
}
public void setShop(ShopModel shop) {
    this.shop = shop;
}
public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id = id;
}
public String getProductName() {
    return productName;
}
public void setProductName(String productName) {
    this.productName = productName;
}
public Double getPrice() {
    return price;
}
public void setPrice(Double price) {
    this.price = price;
}


}
