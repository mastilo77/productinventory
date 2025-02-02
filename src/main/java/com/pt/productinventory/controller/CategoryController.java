package com.pt.productinventory.controller;

import com.pt.productinventory.model.enums.SortDirection;
import com.pt.productinventory.model.dto.CategoryRequestDto;
import com.pt.productinventory.model.dto.CategoryResponseDto;
import com.pt.productinventory.model.dto.CategoryUpdateDto;
import com.pt.productinventory.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
@Tag(name = "Category Management API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create new category")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> save(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(categoryService.save(categoryRequestDto));
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Get all categories paginated")
    @GetMapping("/paginated")
    public ResponseEntity<Page<CategoryResponseDto>> findAllPageable(@RequestParam(defaultValue = "0") Integer pageNum,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                                     @RequestParam(defaultValue = "name") String sortBy,
                                                                     @RequestParam(defaultValue = "ASC") SortDirection sortDirection) {
        return ResponseEntity.ok(categoryService.findAllPageable(pageNum, pageSize, sortBy, sortDirection));
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findByIdResponseDto(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.findByIdResponseDto(id));

    }

    @Operation(summary = "Search category by name")
    @GetMapping("/search")
    public ResponseEntity<CategoryResponseDto> findByName(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(categoryService.findByName(name));

    }

    @Operation(summary = "Update category by id")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        return ResponseEntity.ok(categoryService.update(id, categoryUpdateDto));

    }

    @Operation(summary = "Delete category by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
