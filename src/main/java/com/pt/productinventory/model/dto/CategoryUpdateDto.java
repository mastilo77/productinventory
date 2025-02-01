package com.pt.productinventory.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateDto {
    private String name;
    private List<Long> productIds;
}
