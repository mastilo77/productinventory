package com.pt.productinventory.controller;

import com.pt.productinventory.model.ProductFilter;
import com.pt.productinventory.model.SortDirection;
import com.pt.productinventory.model.dto.ProductRequestDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import com.pt.productinventory.model.dto.ProductUpdateDto;
import com.pt.productinventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
@Tag(name = "Product Management API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create new product")
    @PostMapping
    public ResponseEntity<ProductResponseDto> save(ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.save(productRequestDto));
    }

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());

    }

    @Operation(summary = "Get all products paginated")
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDto>> findAllPageable(@RequestParam(defaultValue = "0") Integer pageNum,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "ASC") SortDirection sortDirection,
                                                                    @RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) Double minPrice,
                                                                    @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(productService.findAllPageable(pageNum, pageSize, sortBy, sortDirection, name, minPrice, maxPrice));
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.findByIdResponseDto(id));
    }

    @Operation(summary = "Update product by id")
    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable("id") Long id,
                                                     @Valid @RequestBody ProductUpdateDto productUpdateDto) {
        return ResponseEntity.ok(productService.update(id, productUpdateDto));
    }

    @Operation(summary = "Delete product by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        productService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add Category to product")
    @PostMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<Void> addCategoryToProduct(@PathVariable("productId") Long productId,
                                                     @PathVariable("categoryId") Long categoryId) {
        productService.addCategoryToProduct(categoryId, productId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove Category from product")
    @DeleteMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromProduct(@PathVariable("productId") Long productId,
                                                          @PathVariable("categoryId") Long categoryId) {
        productService.removeCategoryFromProduct(categoryId, productId);

        return ResponseEntity.ok().build();
    }
}
