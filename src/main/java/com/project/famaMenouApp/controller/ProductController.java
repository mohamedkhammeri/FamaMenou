package com.project.famaMenouApp.controller;

import com.project.famaMenouApp.exception.BadRequestAlertException;
import com.project.famaMenouApp.model.dto.CustomUserDetails;
import com.project.famaMenouApp.model.dto.PartialUpdateProductDTO;
import com.project.famaMenouApp.model.dto.ProductRequest;
import com.project.famaMenouApp.model.entity.Product;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.ProductRepository;
import com.project.famaMenouApp.service.ProductService;
import com.project.famaMenouApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductRepository productRepository;


    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts(
            ) {
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("product/{shopId}")
    public ResponseEntity<List<Product>> getAllProductsByShop(
            @PathVariable Long shopId) {
        return ResponseEntity.ok(productService.getProductsByShop(shopId));
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping("/create/{shopId}")
    public ResponseEntity<Product> createProduct(
            @PathVariable Long shopId,
            @RequestBody ProductRequest productRequest) throws URISyntaxException {

        if (productRequest.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID");
        }



        Product result = productService.createProduct(productRequest,shopId, productRequest.getCategoryId());

        return ResponseEntity.created(new URI("/api/shops/" + shopId + "/products/" + result.getId()))
                .body(result);
    }

    @PatchMapping("/update/{productId}")
    public ResponseEntity<Product> applyPartialUpdate(

            @PathVariable Long productId,
            @RequestBody PartialUpdateProductDTO dto) {

        Product updatedProduct = productService.applyPartialUpdate(productId, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long shopId,
            @PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public void test() {
        Optional<User> isUser = userService.getUserWithAuthorities();
        User user = isUser.get();
        System.out.println(user.getId());

        //Long ownerId = userDetails.getUser().getId();
        //System.out.println(ownerId);
    }



}