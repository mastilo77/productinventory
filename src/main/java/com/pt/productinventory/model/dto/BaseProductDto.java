package com.pt.productinventory.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProductDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
