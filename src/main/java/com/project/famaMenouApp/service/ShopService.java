package com.project.famaMenouApp.service;

import com.project.famaMenouApp.exception.EntityNotFoundException;
import com.project.famaMenouApp.exception.DuplicateEntityException;
import com.project.famaMenouApp.model.entity.Shop;
import com.project.famaMenouApp.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Shop> getPremiumShops() {
        return shopRepository.findByIsPremiumTrue();
    }

    @Transactional(readOnly = true)
    public List<Shop> getShopsByOwner(Long ownerId) {
        return shopRepository.findByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    public Shop getShopById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Shop getShopByIdAndOwner(Long id, Long ownerId) {
        return shopRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found or you don't have permission"));
    }

    @Transactional
    public Shop createShop(Shop shop) {
        if (shopRepository.existsByNameAndAddress(shop.getName(), shop.getAddress())) {
            throw new DuplicateEntityException("Shop with this name and address already exists");
        }
        return shopRepository.save(shop);
    }

    @Transactional
    public Shop updateShop(Long id, Shop shopDetails) {
        Shop shop = getShopById(id);
        shop.setName(shopDetails.getName());
        shop.setAddress(shopDetails.getAddress());
        shop.setPhone(shopDetails.getPhone());
        shop.setLatitude(shopDetails.getLatitude());
        shop.setLongitude(shopDetails.getLongitude());
        shop.setPremium(shopDetails.isPremium());
        shop.setSecretDiscountCode(shopDetails.getSecretDiscountCode());
        shop.setImageUrl(shopDetails.getImageUrl());
        return shopRepository.save(shop);
    }

    @Transactional
    public Shop updateShopByOwner(Long id, Long ownerId, Shop shopDetails) {
        Shop shop = getShopByIdAndOwner(id, ownerId);
        shop.setName(shopDetails.getName());
        shop.setAddress(shopDetails.getAddress());
        shop.setPhone(shopDetails.getPhone());
        shop.setLatitude(shopDetails.getLatitude());
        shop.setLongitude(shopDetails.getLongitude());
        shop.setPremium(shopDetails.isPremium());
        shop.setSecretDiscountCode(shopDetails.getSecretDiscountCode());
        shop.setImageUrl(shopDetails.getImageUrl());
        return shopRepository.save(shop);
    }

    @Transactional
    public void deleteShop(Long id) {
        Shop shop = getShopById(id);
        shopRepository.delete(shop);
    }

    @Transactional
    public void deleteShopByOwner(Long id, Long ownerId) {
        Shop shop = getShopByIdAndOwner(id, ownerId);
        shopRepository.delete(shop);
    }

    @Transactional(readOnly = true)
    public List<Shop> getNearbyShops(Double latitude, Double longitude, Double radiusKm) {
        return shopRepository.findNearbyShops(latitude, longitude, radiusKm);
    }

    @Transactional(readOnly = true)
    public Long countShopsByOwner(Long ownerId) {
        return shopRepository.countByOwnerId(ownerId);
    }
}