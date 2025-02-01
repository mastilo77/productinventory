package com.pt.productinventory.service;

import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.dto.CategoryRequestDto;
import com.pt.productinventory.model.dto.CategoryResponseDto;
import com.pt.productinventory.model.dto.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);
    Iterable<CategoryResponseDto> findAll();
    Page<CategoryResponseDto> findAllPageable(Pageable pageable);
    CategoryResponseDto findByIdResponseDto(Long id);
    Category findById(Long id);
    CategoryResponseDto findByName(String name);
    CategoryResponseDto update(Long id, CategoryUpdateDto categoryUpdateDto);
    void deleteById(Long id);
}
