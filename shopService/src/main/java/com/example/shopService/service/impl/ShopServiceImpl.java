package com.example.shopService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.example.shopService.constants.Category;

import com.example.shopService.entity.ShopModel;
import com.example.shopService.exception.ShopNotFoundException;

import com.example.shopService.repository.ShopRepository;
import com.example.shopService.service.ShopServices;

import io.github.bucket4j.Bucket;
import jakarta.persistence.criteria.Predicate;

@Service
public class ShopServiceImpl implements ShopServices {
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public ShopModel saveShopModel(ShopModel shopModel) {
        if (shopModel.getLatitude() < -90 || shopModel.getLatitude() > 90) {
            throw new IllegalArgumentException("Invalid Latitude");
        }
        return shopRepository.save(shopModel);

    }

    @Override
    public List<ShopModel> getShopModelByCategory(Category category) {
        return shopRepository.findByCategory(category);

    }

    @SuppressWarnings("null")
    @Override
    public ShopModel getShopModelById(Long id) {
        return shopRepository.findById(id)

                .orElseThrow(() -> new ShopNotFoundException("Shop with ID " + id + " not found!"));
    }

    @SuppressWarnings("null")
    @Override
    public ShopModel deleteShopModelById(Long id) {
        ShopModel shopModel = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException("Shop with ID " + id + " not found!"));
        shopRepository.deleteById(id);
        return shopModel;
    }

    @Override
    public List<ShopModel> getAllShopModel() {
        return shopRepository.findAll();
    }
       @Override
       public ShopModel updateShopModel(Long id, ShopModel shopDetails){
        @SuppressWarnings("null")
        ShopModel existShopModel=shopRepository.findById(id)
        .orElseThrow(() -> new ShopNotFoundException("Shop not found with id: " + id));
    existShopModel.setName(shopDetails.getName());
    existShopModel.setAddress(shopDetails.getAddress());
    existShopModel.setCategory(shopDetails.getCategory());
    existShopModel.setLatitude(shopDetails.getLatitude());
    existShopModel.setLongitude(shopDetails.getLongitude());
    return shopRepository.save(existShopModel) ; 
    
    
    }
    @SuppressWarnings("null")
    public Page<ShopModel> getFilteredShopModel(String category, String address, Pageable pageable) {
        return shopRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));

            }
            if (address != null && !address.isEmpty()) {
                predicates.add(cb.equal(root.get("address"), address));

            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public List<ShopModel> getShopModelByCoordinates(Double lat, Double lng) {
        List<ShopModel> shop=shopRepository.findByLatitudeAndLongitude(lat, lng);

        if(shop.isEmpty()){
            throw new ShopNotFoundException("No shop exists at these exact coordinates!");
        }
        return shop;
    }
    
    @Override
    public List<ShopModel> getNearbyShops(Double lat, Double lng, Double radiusInMeters) {
        List<ShopModel> nearbyShops = shopRepository.findNearbyShops(lat, lng, radiusInMeters);
        
        if (nearbyShops.isEmpty()) {
            throw new ShopNotFoundException("No shops found within " + radiusInMeters + " meters of the given location!");
        }
        
        return nearbyShops;
    }
    // 1 min mein 10 request are allow
    private final Bucket createbucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
            .build();

    @Override
    public boolean tryConsumeCreate() {
        return createbucket.tryConsume(1);
    }


    // 1 min mein 5 request allow
    private final Bucket readbucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
            .build();

    @Override
    public boolean tryConsumeRead() {
        return readbucket.tryConsume(1);
    }

    // 1 min mein 2 request allow
    private final Bucket deleteBucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(2).refillIntervally(2, Duration.ofMinutes(1)))
            .build();

    @Override
    public boolean tryConsumeDelete() {
        return deleteBucket.tryConsume(1);
    }






}
