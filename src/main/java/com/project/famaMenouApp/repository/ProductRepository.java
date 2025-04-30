package com.project.famaMenouApp.repository;

import com.project.famaMenouApp.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopId(Long shopId);

    List<Product> findByShopIdAndShopOwnerId(Long shopId, Long ownerId);
}