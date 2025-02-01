package com.pt.productinventory.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {
    private String name;
    private List<BaseProductDto> baseProductDtoList;

    public CategoryResponseDto(String name) {
        this.name = name;
    }
}
