package com.pt.productinventory.service;

import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.enums.SortDirection;
import com.pt.productinventory.model.dto.ProductRequestDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import com.pt.productinventory.model.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponseDto save(ProductRequestDto productRequestDto);

    List<ProductResponseDto> findAll();

    Page<ProductResponseDto> findAllPageable(Integer pageNum,
                                             Integer pageSize,
                                             String sortBy,
                                             SortDirection sortDirection,
                                             String name,
                                             Double minPrice,
                                             Double maxPrice);

    ProductResponseDto findByIdResponseDto(Long id);

    Product findById(Long id);

    ProductResponseDto update(Long productId, ProductUpdateDto productUpdateDto);

    void deleteById(Long id);

    void addCategoryToProduct(Long categoryId, Long productId);

    void removeCategoryFromProduct(Long categoryId, Long productId);
}
