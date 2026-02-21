package com.example.shopService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shopService.entity.ShopModel;
import com.example.shopService.constants.Category;

public interface ShopRepository extends JpaRepository<ShopModel,Long>,JpaSpecificationExecutor<ShopModel>{
    List<ShopModel> findByCategory(Category category);
    List<ShopModel> findByLatitudeAndLongitude(Double latitude, Double longitude);
    Page<ShopModel> findByCategoryContainingIgnoreCaseAndAddressContainingIgnoreCase(
        String category, String address, Pageable pageable);
    
    // Find shops within given radius (in meters) from given coordinates
    @Query("SELECT s FROM ShopModel s WHERE " +
           "(6371000 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * " +
           "cos(radians(s.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(s.latitude)))) < :radius")
    List<ShopModel> findNearbyShops(@Param("lat") Double lat, @Param("lng") Double lng, @Param("radius") Double radius);
}