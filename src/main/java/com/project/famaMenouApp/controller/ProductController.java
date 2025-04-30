package com.project.famaMenouApp.controller;

import com.project.famaMenouApp.exception.BadRequestAlertException;
import com.project.famaMenouApp.model.entity.Product;
import com.project.famaMenouApp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/shops/{shopId}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProductsByShop(
            @PathVariable Long shopId) {
        return ResponseEntity.ok(productService.getProductsByShop(shopId));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Product>> getAllProductsByShopAndOwner(
            @PathVariable Long shopId,
            @RequestParam Long ownerId) {
        return ResponseEntity.ok(productService.getProductsByShopAndOwner(shopId, ownerId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long shopId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @PathVariable Long shopId,
            @RequestBody Product product) throws URISyntaxException {
        if (product.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID");
        }

        Product result = productService.createProduct(product, shopId);
        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/products/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long shopId,
            @PathVariable Long productId,
            @RequestBody Product product) {
        if (product.getId() == null) {
            throw new BadRequestAlertException("Invalid id");
        }

        if (!productId.equals(product.getId())) {
            throw new BadRequestAlertException("Invalid ID");
        }

        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long shopId,
            @PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}