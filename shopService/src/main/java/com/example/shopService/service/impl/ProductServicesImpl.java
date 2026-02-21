package com.example.shopService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopService.repository.ShopRepository;
import com.example.shopService.entity.ProductModel;
import com.example.shopService.entity.ShopModel;
import com.example.shopService.exception.ShopNotFoundException;
import com.example.shopService.exception.ProductNotFoundException;
import com.example.shopService.repository.ProductRepository;
import com.example.shopService.service.ProductServices;

import java.time.Duration;
import java.util.List;
import io.github.bucket4j.Bucket;
import org.springframework.lang.NonNull;
@Service
public class ProductServicesImpl implements ProductServices {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;

    // Rate limiting buckets
    private final Bucket readBucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
            .build();

    private final Bucket createBucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
            .build();

    private final Bucket deleteBucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(3).refillGreedy(3, Duration.ofMinutes(1)))
            .build();
    @Override
    public ProductModel addProductToShop(@NonNull Long shopId, @NonNull ProductModel product) {
   //find shop
    @SuppressWarnings("null")
    ShopModel shop=shopRepository.findById(shopId)
    .orElseThrow(() -> new ShopNotFoundException("Shop Not found with ID: " + shopId));
    //tell product owner shop
    product.setShop(shop);
//add product in shop product list
shop.getProducts().add(product);
//save the product
return productRepository.save(product);

}
//------update product--------
    @Override
    public ProductModel updateProduct(@NonNull Long shopId, @NonNull Long productId, @NonNull ProductModel updatedDetails) {



// check product id

@SuppressWarnings("null")
ProductModel product=productRepository.findById(productId)
.orElseThrow(()-> new ProductNotFoundException("Product not found with ID: " + productId));

//validate product
if(!product.getShop().getId().equals(shopId)){
    throw new ProductNotFoundException("This product does not belong to this shop!");
}

// update the details
product.setProductName(updatedDetails.getProductName());
product.setPrice(updatedDetails.getPrice());
return productRepository.save(product);
}

//-----------delete Product--------
@SuppressWarnings("null")
    @Override
    public ProductModel deleteProduct(@NonNull Long shopId, @NonNull Long productId) {
    ProductModel product=productRepository.findById(productId)
    .orElseThrow(()-> new ProductNotFoundException("Product not found with ID: " + productId));
//validation
if(!product.getShop().getId().equals(shopId)){
    throw new ProductNotFoundException("Unauthorized delete attempt - product does not belong to this shop!");
}
 productRepository.delete(product);
 return product;
}

    @Override
    public List<ProductModel> getProductsByShop(@NonNull Long shopId) {
    // Verify shop exists
    shopRepository.findById(shopId)
            .orElseThrow(() -> new ShopNotFoundException("Shop not found with ID: " + shopId));
    
    return productRepository.findByShopId(shopId);
}

    @Override
    public ProductModel getProductById(@NonNull Long productId) {
    return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
}

    @Override
    public List<ProductModel> searchProductsByName(@NonNull String name) {
    return productRepository.findByProductNameContainingIgnoreCase(name);
}

// Rate limiting implementations
@Override
public boolean tryConsumeRead() {
    return readBucket.tryConsume(1);
}

@Override
public boolean tryConsumeCreate() {
    return createBucket.tryConsume(1);
}

@Override
public boolean tryConsumeDelete() {
    return deleteBucket.tryConsume(1);
}
}
