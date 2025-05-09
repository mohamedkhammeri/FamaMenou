package com.project.famaMenouApp.controller;

import com.project.famaMenouApp.exception.EntityNotFoundException;
import com.project.famaMenouApp.model.entity.Shop;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.UserRepository;
import com.project.famaMenouApp.service.ShopService;
import com.project.famaMenouApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/premium")
    public ResponseEntity<List<Shop>> getPremiumShops() {
        return ResponseEntity.ok(shopService.getPremiumShops());
    }

    @GetMapping("/my-shops")
    public ResponseEntity<List<Shop>> getShopsByOwner(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user=  userService.getUserByLogin(userDetails.getUsername());
        return ResponseEntity.ok(shopService.getShopsByOwner(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    @PostMapping
    public ResponseEntity<Shop> createShop(
            @RequestBody Shop shop,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get the authenticated user
        User user = userRepository.findOneByLoginIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Set the owner on the shop
        shop.setOwner(user);

        Shop createdShop = shopService.createShop(shop);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdShop.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdShop);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(
            @PathVariable Long id,
            @RequestBody Shop shopDetails) {
        return ResponseEntity.ok(shopService.updateShop(id, shopDetails));
    }

    @PutMapping("/{id}/owner")
    public ResponseEntity<Shop> updateShopByOwner(
            @PathVariable Long id,
            @RequestBody Shop shopDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long ownerId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(shopService.updateShopByOwner(id, ownerId, shopDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/owner")
    public ResponseEntity<Void> deleteShopByOwner(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long ownerId = Long.parseLong(userDetails.getUsername());
        shopService.deleteShopByOwner(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}