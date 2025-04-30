package com.project.famaMenouApp.repository;

import com.project.famaMenouApp.model.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    // Find shops by owner ID
    List<Shop> findByOwnerId(Long ownerId);

    // Find shop by ID and owner ID
    Optional<Shop> findByIdAndOwnerId(Long id, Long ownerId);

    // Find premium shops - matches the isPremium field in entity
    List<Shop> findByIsPremiumTrue();

    // Check if shop with same name and address exists
    boolean existsByNameAndAddress(String name, String address);

    // Find shops within geographic radius (using Haversine formula)
    @Query(value = "SELECT * FROM shops WHERE " +
            "6371 * acos(" +
            "cos(radians(:latitude)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(latitude))" +
            ") < :radius", nativeQuery = true)
    List<Shop> findNearbyShops(Double latitude, Double longitude, Double radius);

    // Count shops by owner
    Long countByOwnerId(Long ownerId);

    // Additional useful methods
    List<Shop> findAllByOrderByNameAsc();
    List<Shop> findByNameContainingIgnoreCase(String name);
}