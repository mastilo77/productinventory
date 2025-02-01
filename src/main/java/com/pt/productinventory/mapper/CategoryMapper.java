package com.pt.productinventory.mapper;

import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.dto.BaseProductDto;
import com.pt.productinventory.model.dto.CategoryResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryMapper {

    private final ProductMapper productMapper;

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        List<BaseProductDto> products = category.getProducts()
                .stream()
                .map(productMapper::toBaseProductDto)
                .toList();

        return CategoryResponseDto.builder()
                .name(category.getName())
                .baseProductDtoList(products)
                .build();
    }
}
