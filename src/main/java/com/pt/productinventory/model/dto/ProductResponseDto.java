package com.pt.productinventory.model.dto;

import com.pt.productinventory.model.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String category;
}
