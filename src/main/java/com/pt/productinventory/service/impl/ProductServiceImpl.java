package com.pt.productinventory.service.impl;

import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import com.pt.productinventory.mapper.ProductMapper;
import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.ProductFilter;
import com.pt.productinventory.model.enums.SortDirection;
import com.pt.productinventory.model.dto.ProductRequestDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import com.pt.productinventory.model.dto.ProductUpdateDto;
import com.pt.productinventory.repository.ProductRepository;
import com.pt.productinventory.service.CategoryService;
import com.pt.productinventory.service.ProductService;
import com.pt.productinventory.validator.ValidatorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
                .build();

        validatorService.validate(product);

        return productMapper.toProductResponseDto(save(product));
    }

    @Override
    public List<ProductResponseDto> findAll() {
        log.debug("calling findAll method in {}", className);

        return productRepository.findAll().stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponseDto> findAllPageable(Integer pageNum,
                                                    Integer pageSize,
                                                    String sortBy,
                                                    SortDirection sortDirection,
                                                    String name,
                                                    Double minPrice,
                                                    Double maxPrice) {
        log.debug("calling findAllPageable method in {}", className);

        validatorService.validateClassField(Product.class, sortBy);

        ProductFilter productFilter = ProductFilter.builder()
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        Specification<Product> productSpecification = createFilterSpecification(productFilter);

        Sort sortByDirection = Objects.equals(sortDirection.name(), SortDirection.ASC.name())
                               ? Sort.by(sortBy).ascending()
                               : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sortByDirection);

        return productRepository.findAll(productSpecification, pageRequest)
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
    @Transactional
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

        productRepository.deleteById(id);

        log.info("Successfully deleted product with id: {}", id);
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

        category.getProducts().removeIf(p -> p.getId().equals(productId));
        product.setCategory(null);

        save(product);
    }

    @Transactional
    private Product save(Product product) {
        log.debug("calling save method in {}", className);

        return productRepository.save(product);
    }

    private Specification<Product> createFilterSpecification(ProductFilter filter) {
        log.debug("calling createFilterSpecification method in {}", className);

        Specification<Product> spec = Specification.where(null);

        if (filter.getName() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
        }
        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        return spec;
    }
}
