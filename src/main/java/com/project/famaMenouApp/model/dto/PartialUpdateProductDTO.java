package com.project.famaMenouApp.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartialUpdateProductDTO {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Long categoryId;
}
