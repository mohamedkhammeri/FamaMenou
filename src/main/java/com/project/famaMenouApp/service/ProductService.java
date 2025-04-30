package com.project.famaMenouApp.service;

import com.project.famaMenouApp.exception.EntityNotFoundException;
import com.project.famaMenouApp.model.entity.Product;
import com.project.famaMenouApp.model.entity.Shop;
import com.project.famaMenouApp.repository.ProductRepository;
import com.project.famaMenouApp.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    // Basic CRUD operations

    @Transactional
    public Product createProduct(Product product, Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found"));
        product.setShop(shop);
        return productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product updateProduct(Long productId, Product productDetails) {
        Product existingProduct = getProductById(productId);
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setImageUrl(productDetails.getImageUrl());
        existingProduct.setActive(productDetails.isActive());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    // Special query methods

    public List<Product> getProductsByShop(Long shopId) {
        return productRepository.findByShopId(shopId);
    }

    public List<Product> getProductsByShopAndOwner(Long shopId, Long ownerId) {
        return productRepository.findByShopIdAndShopOwnerId(shopId, ownerId);
    }
}