package com.project.famaMenouApp.service;

import com.project.famaMenouApp.exception.EntityNotFoundException;
import com.project.famaMenouApp.model.dto.PartialUpdateProductDTO;
import com.project.famaMenouApp.model.dto.ProductRequest;
import com.project.famaMenouApp.model.entity.*;
import com.project.famaMenouApp.repository.ProductRepository;
import com.project.famaMenouApp.repository.ShopRepository;
import com.project.famaMenouApp.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ShopRepository shopRepository;
    private final ProductCategoryRepository categoryRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ShopRepository shopRepository, ProductCategoryRepository categoryRepository, UserService userService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }
// Basic CRUD operations

    @Transactional
    public Product createProduct(ProductRequest productRequest , Long shopId, Long categoryId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found"));
        Optional<User> user=userService.getUserWithAuthorities();

        if (user.isPresent()) {
            Product product = new Product();
            product.setShop(shop);
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setOwner(user.get());
            if (categoryId != null) {
                ProductCategory category = productCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
                product.setCategory(category);
            }

            // Create initial price history entry
        PriceHistory initialPrice = new PriceHistory();
        initialPrice.setPrice(product.getPrice());
        initialPrice.setRecordedAt(LocalDateTime.now());
        initialPrice.setProduct(product);
        product.getPriceHistory().add(initialPrice);
        Product result= productRepository.save(product);

        return result;
        }
        else {
            throw new SecurityException("Unauthorized access");
        }
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Transactional
    public Product applyPartialUpdate(Long productId, PartialUpdateProductDTO dto) {
        Long ownerId = userService.getUserWithAuthorities()
                .orElseThrow(() -> new SecurityException("Unauthorized access"))
                .getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

        if (!product.getOwner().getId().equals(ownerId)) {
            throw new SecurityException("You are not authorized to update this product");
        }

        // Apply partial updates
        Optional.ofNullable(dto.getName()).ifPresent(product::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(dto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(dto.getImageUrl()).ifPresent(product::setImageUrl);

        if (dto.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category with ID " + dto.getCategoryId() + " not found"));
            product.setCategory(category);
        }

        return productRepository.save(product);
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