package com.example.shopService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopService.entity.ProductModel;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long>{
    
    List<ProductModel> findByShopId(Long shopId);
    List<ProductModel> findByProductNameContainingIgnoreCase(String name);
}
