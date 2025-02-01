package com.pt.productinventory.service.impl;

import com.pt.productinventory.error.exceptions.IllegalParameterException;
import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import com.pt.productinventory.mapper.ProductMapper;
import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.dto.ProductRequestDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import com.pt.productinventory.model.dto.ProductUpdateDto;
import com.pt.productinventory.repository.ProductRepository;
import com.pt.productinventory.service.CategoryService;
import com.pt.productinventory.service.ProductService;
import com.pt.productinventory.validator.ValidatorService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ValidatorService validatorService;
    private final ProductMapper productMapper;
    private final String className = this.getClass().getSimpleName();

    @Override
    @Transactional
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        log.debug("calling save method in {}", className);

        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .quantity(productRequestDto.getQuantity())
                .category(categoryService.findById(productRequestDto.getCategoryId()))
                .build();

        validatorService.validate(product);

        return productMapper.toProductResponseDto(save(product));
    }

    @Override
    public Iterable<ProductResponseDto> findAll() {
        log.debug("calling findAll method in {}", className);

        return productRepository.findAll().stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponseDto> findAllPageable(Pageable pageable) {
        log.debug("calling findAllPageable method in {}", className);

        return productRepository.findAll(pageable)
                .map(productMapper::toProductResponseDto);
    }

    @Override
    public ProductResponseDto findByIdResponseDto(Long id) {
        log.debug("calling findByIdResponseDto method in {}", className);

        return productMapper.toProductResponseDto(findById(id));
    }

    @Override
    public Product findById(Long id) {
        log.debug("calling findById method in {}", className);

        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + id));
    }

    @Override
    public ProductResponseDto findByName(String name) {
        log.debug("calling findByName method in {}", className);

        if (StringUtils.isBlank(name)) {
            throw new IllegalParameterException("Name parameter is not valid: " + name);
        }

        return productRepository.findByName(name)
                .map(productMapper::toProductResponseDto)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with name: " + name));
    }

    @Override
    public ProductResponseDto update(Long productId, ProductUpdateDto productUpdateDto) {
        log.debug("calling update method in {}", className);

        Product product = findById(productId);

        product.setName(productUpdateDto.getName());
        product.setDescription(productUpdateDto.getDescription());
        product.setPrice(productUpdateDto.getPrice());
        product.setQuantity(productUpdateDto.getQuantity());
        Optional.ofNullable(productUpdateDto.getCategoryId())
                .ifPresentOrElse(categoryId -> {
                    Category category = categoryService.findById(categoryId);
                    product.setCategory(category);
                    if (!category.getProducts().contains(product)) {
                        category.getProducts().add(product);
                    }
                }, () -> {
                    throw new ObjectNotFoundException("Category not found with id: " + productUpdateDto.getCategoryId());
                });

        return productMapper.toProductResponseDto(save(product));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("calling deleteById method in {}", className);

        productRepository.findById(id)
                .ifPresentOrElse(product -> {
                    log.info("product found with id: {}", id);
                    productRepository.deleteById(product.getId());
                    log.info("product deleted successfully!");
                }, () -> log.info("Unable to complete deletion! Product not found with id: {}", id));
    }

    @Override
    @Transactional
    public void addCategoryToProduct(Long categoryId, Long productId) {
        log.debug("calling addCategoryToProduct method in {}", className);

        Category category = categoryService.findById(categoryId);
        Product product = findById(productId);
        product.setCategory(category);
        category.getProducts().add(product);

        save(product);
    }

    @Override
    @Transactional
    public void removeCategoryFromProduct(Long categoryId, Long productId) {
        log.debug("calling removeCategoryFromProduct method in {}", className);

        Category category = categoryService.findById(categoryId);
        Product product = findById(productId);

        category.getProducts().remove(product);
        product.setCategory(null);

        save(product);
    }

    @Transactional
    private Product save(Product product) {
        log.debug("calling save method in {}", className);

        return productRepository.save(product);
    }
}
