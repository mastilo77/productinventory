package com.pt.productinventory.service;

import com.pt.productinventory.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Category save(Category category);
    Iterable<Category> findAll();
    Page<Category> findAllPageable(Pageable pageable);
    Category findById(Long id);
    Category findByName(String name);
    void deleteById(Long id);
    void addCategoryToProduct(Long categoryId, Long productId);
}
