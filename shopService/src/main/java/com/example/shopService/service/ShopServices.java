package com.example.shopService.service;


import com.example.shopService.constants.Category;
import com.example.shopService.entity.ShopModel;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopServices {
    ShopModel saveShopModel(ShopModel shopModel);

    List<ShopModel> getAllShopModel();

    List<ShopModel> getShopModelByCategory(Category category);

    ShopModel getShopModelById(Long id);
    ShopModel deleteShopModelById(Long id);
    ShopModel updateShopModel(Long id, ShopModel shopDetails);

    Page<ShopModel> getFilteredShopModel(String category, String address, Pageable pageable);

    List<ShopModel> getShopModelByCoordinates(Double lat, Double lng);
    
    List<ShopModel> getNearbyShops(Double lat, Double lng, Double radiusInMeters);

    boolean tryConsumeCreate();
    boolean tryConsumeRead();
    boolean tryConsumeDelete();
   

}