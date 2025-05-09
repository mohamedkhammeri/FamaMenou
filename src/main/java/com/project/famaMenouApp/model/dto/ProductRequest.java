package com.project.famaMenouApp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductRequest {
    Long id;
    String name;
    String description;
    Double price;
    Double rating;
    String imageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long categoryId;
    Long shopId;

    @Override
    public String toString() {
        return "ProductRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryId=" + categoryId +
                ", shopId=" + shopId +
                '}';
    }
}
