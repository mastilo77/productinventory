package com.pt.productinventory.service.impl;

import com.pt.productinventory.error.exceptions.IllegalParameterException;
import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import com.pt.productinventory.mapper.CategoryMapper;
import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.dto.CategoryRequestDto;
import com.pt.productinventory.model.dto.CategoryResponseDto;
import com.pt.productinventory.model.dto.CategoryUpdateDto;
import com.pt.productinventory.repository.CategoryRepository;
import com.pt.productinventory.repository.ProductRepository;
import com.pt.productinventory.service.CategoryService;
import com.pt.productinventory.validator.ValidatorService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ValidatorService validatorService;
    private final String className = this.getClass().getSimpleName();

    @Override
    @Transactional
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
        log.debug("calling save method in {}", className);

        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .build();

        validatorService.validate(category);

        return categoryMapper.toCategoryResponseDto(save(category));
    }

    @Override
    public Iterable<CategoryResponseDto> findAll() {
        log.debug("calling findAll method in {}", className);

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponseDto)
                .toList();
    }

    @Override
    public Page<CategoryResponseDto> findAllPageable(Pageable pageable) {
        log.debug("calling findAllPageable method in {}", className);

        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toCategoryResponseDto);
    }

    @Override
    public CategoryResponseDto findByIdResponseDto(Long id) {
        log.debug("calling findByIdResponseDto method in {}", className);

        return categoryMapper.toCategoryResponseDto(findById(id));
    }

    @Override
    public Category findById(Long id) {
        log.debug("calling findById method in {}", className);

        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + id));
    }

    @Override
    public CategoryResponseDto findByName(String name) {
        log.debug("calling findByName method in {}", className);

        if (StringUtils.isBlank(name)) {
            throw new IllegalParameterException("Name parameter is not valid: " + name);
        }

        return categoryRepository.findByName(name)
                .map(categoryMapper::toCategoryResponseDto)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with name: " + name));
    }

    @Override
    @Transactional
    public CategoryResponseDto update(Long id, CategoryUpdateDto categoryUpdateDto) {
        log.debug("calling update method in {}", className);

        Category category = findById(id);
        category.setName(categoryUpdateDto.getName());

        if (!CollectionUtils.isEmpty(categoryUpdateDto.getProductIds())) {
            List<Product> products = productRepository.findAllById(categoryUpdateDto.getProductIds()).stream()
                    .map(product -> {
                        product.setCategory(category);
                        return product;
                    })
                    .collect(Collectors.toList());
            category.setProducts(products);
        }

        return categoryMapper.toCategoryResponseDto(save(category));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("calling deleteById method in {}", className);

        categoryRepository.findById(id)
                .ifPresentOrElse(category -> {
                    log.info("category found with id: {}", id);
                    categoryRepository.deleteById(category.getId());
                    log.info("category deleted successfully!");
                }, () -> log.info("Unable to complete deletion! Category not found with id: {}", id));
    }

    @Transactional
    private Category save(Category category) {
        log.debug("calling save method in {}", className);

        return categoryRepository.save(category);
    }
}
