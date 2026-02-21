package com.example.shopService.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopService.entity.ShopModel;
import com.example.shopService.service.ShopServices;
import com.example.shopService.exception.RateLimitException;
import com.example.shopService.constants.Category;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/v1/shops")
public class ShopController {
    @Autowired
    private ShopServices shopServices;

    // see all shops
    //5 request per minute
    @GetMapping("/all")
    public ResponseEntity<?> getAllShopModel() {
        checkReadLimit();
        return ResponseEntity.ok(shopServices.getAllShopModel());
    }

    // get a shop by category
    @GetMapping("/category/{category}")
    public List<ShopModel> getByCategory(@PathVariable Category category) {
       checkReadLimit();
        return shopServices.getShopModelByCategory(category);
    }

    // add a shop
    //10 request per minute
    @PostMapping("/add")
    public ResponseEntity<?> createShopModel(@Valid @RequestBody ShopModel shopModel) {
       checkCreateLimit();
    return ResponseEntity.ok(shopServices.saveShopModel(shopModel));
       
    }

    // find a shop by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopModelById(@PathVariable("id") long id) {
       checkReadLimit();
      ShopModel shop= shopServices.getShopModelById(id);
      return ResponseEntity.ok(shop);
    }
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteShopModelById(@PathVariable ("id") long id){
       checkDeleteLimit();
        shopServices.deleteShopModelById(id);
       return ResponseEntity.noContent().build(); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShopModel(@PathVariable long id, @Valid @RequestBody ShopModel shopModel){
   checkCreateLimit();
    ShopModel updateShopModel=shopServices.updateShopModel(id, shopModel);
      return ResponseEntity.ok(updateShopModel);
    }

    // filter the shop on the bases of address and category
    @GetMapping("/filter")
    public ResponseEntity<?> getFilteredShopModel(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String address,
            Pageable pageable) {
              checkReadLimit();
              return ResponseEntity.ok(  shopServices.getFilteredShopModel(category, address, pageable));
               
    }

    // find the shop by its latitude and longitude
    @GetMapping("/search")
    public ResponseEntity<?> getShopModel(@RequestParam Double lat, @RequestParam Double lng) {
        checkReadLimit();
       
        return ResponseEntity.ok(shopServices.getShopModelByCoordinates(lat, lng));
    }
    
    // find nearby shops within given radius (in meters)
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyShops(
            @RequestParam Double lat, 
            @RequestParam Double lng, 
            @RequestParam(defaultValue = "100") Double radius) {
        checkReadLimit();
        
        return ResponseEntity.ok(shopServices.getNearbyShops(lat, lng, radius));
    }
// --- HELPER METHODS (To avoid repetitive code) ---
private void checkReadLimit(){
   if (!shopServices.tryConsumeRead()) {
            throw new RateLimitException("Read limit exceeded! Too many requests.");
        }
    } 
private void checkCreateLimit(){
    if (!shopServices.tryConsumeCreate()) {
            throw new RateLimitException("Create  limit exceeded! Too many requests.");
        }
    } 
    private void checkDeleteLimit(){
        if (!shopServices.tryConsumeDelete()) {
            throw new RateLimitException("Delete limit exceeded! Too many requests.");
        }
    } 
    
}


