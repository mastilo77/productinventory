package com.pt.productinventory.mapper;

import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.dto.BaseProductDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductMapper {

    public ProductResponseDto toProductResponseDto(Product product) {
        boolean isCategoryEmpty = Objects.isNull(product.getCategory()) || StringUtils.isBlank(product.getCategory().getName());

        return ProductResponseDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(isCategoryEmpty
                          ? null
                          : product.getCategory().getName())
                .build();
    }

    public BaseProductDto toBaseProductDto(Product product) {
        return BaseProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
