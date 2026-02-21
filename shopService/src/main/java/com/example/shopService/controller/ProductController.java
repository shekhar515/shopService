package com.example.shopService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.shopService.entity.ProductModel;
import com.example.shopService.service.ProductServices;
import com.example.shopService.exception.RateLimitException;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

import java.util.List;



@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;
    // add product to shop
    @PostMapping("/shop/{shopId}")
    public ResponseEntity<ProductModel> addProduct(@NonNull @PathVariable Long shopId, @NonNull @Valid @RequestBody ProductModel product) {
        checkCreateLimit();
        ProductModel savedProduct = productServices.addProductToShop(shopId, product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // get all products in a shop
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ProductModel>> getProductsByShop(@NonNull @PathVariable Long shopId) {
        checkReadLimit();
        List<ProductModel> products = productServices.getProductsByShop(shopId);
        return ResponseEntity.ok(products);
    }

    // get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductModel> getProductById(@NonNull @PathVariable Long productId) {
        checkReadLimit();
        ProductModel product = productServices.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // search products by name
    @GetMapping("/search")
    public ResponseEntity<List<ProductModel>> searchProducts(@NonNull @RequestParam String name) {
        checkReadLimit();
        List<ProductModel> products = productServices.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
// update product
    @PutMapping("/{shopId}/products/{productId}")
    public ResponseEntity<ProductModel> updateProduct(@NonNull @PathVariable Long shopId, @NonNull @PathVariable Long productId, @NonNull @Valid @RequestBody ProductModel product) {
        checkCreateLimit();
        return ResponseEntity.ok(productServices.updateProduct(shopId, productId, product));
    }
    // Delete Product
    @DeleteMapping("/{shopId}/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@NonNull @PathVariable Long shopId, @NonNull @PathVariable Long productId) {
        checkDeleteLimit();
        productServices.deleteProduct(shopId, productId);
        return ResponseEntity.noContent().build();
    }

    // --- HELPER METHODS (Rate Limiting) ---
    private void checkReadLimit() {
        if (!productServices.tryConsumeRead()) {
            throw new RateLimitException("Product read limit exceeded! Too many requests.");
        }
    }

    private void checkCreateLimit() {
        if (!productServices.tryConsumeCreate()) {
            throw new RateLimitException("Product create limit exceeded! Too many requests.");
        }
    }

    private void checkDeleteLimit() {
        if (!productServices.tryConsumeDelete()) {
            throw new RateLimitException("Product delete limit exceeded! Too many requests.");
        }
    }

}
