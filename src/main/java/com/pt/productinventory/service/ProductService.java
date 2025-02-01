package com.pt.productinventory.service;

import com.pt.productinventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product save(Product category);

    Iterable<Product> findAll();

    Page<Product> findAllPageable(Pageable pageable);

    Product findById(Long id);

    Product findByName(String name);

    void deleteById(Long id);
}
