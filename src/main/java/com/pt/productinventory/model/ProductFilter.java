package com.pt.productinventory.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductFilter {
    private String name;
    private Double minPrice;
    private Double maxPrice;
}
