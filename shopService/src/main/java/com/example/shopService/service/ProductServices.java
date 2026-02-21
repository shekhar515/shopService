package com.example.shopService.service;

import com.example.shopService.entity.ProductModel;
import java.util.List;
import org.springframework.lang.NonNull;

public interface ProductServices {

    ProductModel addProductToShop(@NonNull Long shopId, @NonNull ProductModel product);
    ProductModel deleteProduct(@NonNull Long shopId, @NonNull Long productId);
    ProductModel updateProduct(@NonNull Long shopId, @NonNull Long productId, @NonNull ProductModel product);
    
    // Additional read operations
    List<ProductModel> getProductsByShop(@NonNull Long shopId);
    ProductModel getProductById(@NonNull Long productId);
    List<ProductModel> searchProductsByName(@NonNull String name);
    
    // Rate limiting operations
    boolean tryConsumeRead();
    boolean tryConsumeCreate();
    boolean tryConsumeDelete();
}
