package com.pt.productinventory.service;

import com.pt.productinventory.error.exceptions.ObjectNotFoundException;
import com.pt.productinventory.mapper.ProductMapper;
import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.Product;
import com.pt.productinventory.model.SortDirection;
import com.pt.productinventory.model.dto.ProductRequestDto;
import com.pt.productinventory.model.dto.ProductResponseDto;
import com.pt.productinventory.model.dto.ProductUpdateDto;
import com.pt.productinventory.repository.ProductRepository;
import com.pt.productinventory.service.impl.CategoryServiceImpl;
import com.pt.productinventory.service.impl.ProductServiceImpl;
import com.pt.productinventory.validator.ValidatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private ValidatorService validatorService;
    @Mock
    private ProductMapper productMapper;

    Category fruit, hardware;
    Product apple, pear, wirelessHeadphones;
    ProductResponseDto appleResponseDto, pearResponseDto, wirelessHeadphonesResponseDto;
    ProductRequestDto appleRequestDto, pearRequestDto, wirelessHeadphonesRequestDto;

    @BeforeEach
    void initData() {
        fruit = Category.builder()
                .id(0L)
                .name("fruit")
                .build();
        hardware = Category.builder()
                .id(1L)
                .name("hardware")
                .build();

        apple = Product.builder()
                .id(0L)
                .name("Apple")
                .price(25.99)
                .quantity(15)
                .description("This is a non GMO apple!")
                .build();
        pear = Product.builder()
                .id(1L)
                .name("Pear")
                .price(35.00)
                .quantity(10)
                .description("This is a non GMO pear!")
                .category(fruit)
                .build();
        fruit.getProducts().addAll(List.of(apple, pear));
        wirelessHeadphones = Product.builder()
                .id(2L)
                .name("Wireless Headphones")
                .price(49.99)
                .quantity(1)
                .description("Bluetooth wireless headphones!")
                .category(hardware)
                .build();
        hardware.getProducts().add(wirelessHeadphones);

        appleResponseDto = ProductResponseDto.builder()
                .name(apple.getName())
                .description(apple.getDescription())
                .price(apple.getPrice())
                .quantity(apple.getQuantity())
                .build();

        pearResponseDto = ProductResponseDto.builder()
                .name(pear.getName())
                .description(pear.getDescription())
                .price(pear.getPrice())
                .quantity(pear.getQuantity())
                .category(pear.getCategory().getName())
                .build();

        wirelessHeadphonesResponseDto = ProductResponseDto.builder()
                .name(wirelessHeadphones.getName())
                .description(wirelessHeadphones.getDescription())
                .price(wirelessHeadphones.getPrice())
                .quantity(wirelessHeadphones.getQuantity())
                .category(wirelessHeadphones.getCategory().getName())
                .build();

        appleRequestDto = ProductRequestDto.builder()
                .name(apple.getName())
                .description(apple.getDescription())
                .price(apple.getPrice())
                .quantity(apple.getQuantity())
                .build();

        pearRequestDto = ProductRequestDto.builder()
                .name("Pear")
                .price(35.00)
                .quantity(10)
                .description("This is a non GMO pear!")
                .build();

        wirelessHeadphonesRequestDto = ProductRequestDto.builder()
                .name("Wireless Headphones")
                .price(49.99)
                .quantity(1)
                .description("Bluetooth wireless headphones!")
                .build();
    }

    @Test
    void should_SaveProduct() {

        Assertions.assertDoesNotThrow(() -> validatorService.validate(apple));
        when(productRepository.save(any(Product.class)))
                .thenReturn(apple);
        when(productMapper.toProductResponseDto(any(Product.class)))
                .thenReturn(appleResponseDto);

        ProductResponseDto productResponseDto = productService.save(appleRequestDto);

        assertNotNull(productResponseDto);
        assertEquals(productResponseDto.getName(), apple.getName());
        assertEquals(productResponseDto.getDescription(), apple.getDescription());
        assertEquals(productResponseDto.getPrice(), apple.getPrice());
        assertEquals(productResponseDto.getQuantity(), apple.getQuantity());
        assertEquals(appleResponseDto, productResponseDto);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void should_FindAllProducts() {
        productService.findAll();

        verify(productRepository).findAll();
    }

    @Test
    void should_FindAllPageableProducts() {
        String sortBy = "name";

        Assertions.assertDoesNotThrow(() -> validatorService.validateClassField(Mockito.any(), Mockito.eq(sortBy)));
        int pageNum = 0;
        int pageSize = 10;
        SortDirection sortDirection = SortDirection.ASC;
        String name = "apple";
        Double minPrice = 10.0;
        Double maxPrice = 100.0;

        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).ascending());
        List<Product> productList = List.of(apple);
        Page<Product> productPage = new PageImpl<>(productList, pageRequest, productList.size());

        when(productRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(productPage);
        when(productMapper.toProductResponseDto(any(Product.class))).thenReturn(appleResponseDto);

        Page<ProductResponseDto> result = productService.findAllPageable(pageNum, pageSize, sortBy, sortDirection, name, minPrice, maxPrice);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(validatorService).validateClassField(Product.class, sortBy);
        verify(productRepository).findAll(any(Specification.class), eq(pageRequest));
        verify(productMapper, times(1)).toProductResponseDto(any(Product.class));
    }

    @Test
    void should_FindByIdResponseDtoProduct() {
        when(productRepository.findById(0L)).thenReturn(Optional.of(apple));
        when(productMapper.toProductResponseDto(apple)).thenReturn(appleResponseDto);

        ProductResponseDto result = productService.findByIdResponseDto(0L);

        assertNotNull(result);
        verify(productRepository).findById(0L);
        verify(productMapper).toProductResponseDto(apple);
    }

    @Test
    void should_ThrowNotFound_FindByIdResponseDtoProduct() {
        when(productRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> productService.findById(4L));
    }

    @Test
    void should_UpdateProduct() {
        ProductUpdateDto appleUpdateDto = ProductUpdateDto.builder()
                .name("updated apple name")
                .price(99.99)
                .description("updated apple description")
                .quantity(25)
                .categoryId(0L)
                .build();
        Product updatedApple = Product.builder()
                .name("updated apple name")
                .price(99.99)
                .description("updated apple description")
                .quantity(25)
                .build();
        ProductResponseDto updatedAppleResponseDto = ProductResponseDto.builder()
                .name("updated apple name")
                .price(99.99)
                .description("updated apple description")
                .quantity(25)
                .build();

        when(productRepository.findById(0L))
                .thenReturn(Optional.of(apple));
        when(categoryService.findById(0L))
                .thenReturn(fruit);
        when(productMapper.toProductResponseDto(any(Product.class)))
                .thenReturn(updatedAppleResponseDto);
        when(productRepository.save(any(Product.class)))
                .thenReturn(updatedApple);

        ProductResponseDto result = productService.update(0L, appleUpdateDto);

        assertNotNull(result);
        Assertions.assertEquals(appleUpdateDto.getName(), result.getName());
        Assertions.assertEquals(appleUpdateDto.getQuantity(), result.getQuantity());
        Assertions.assertEquals(appleUpdateDto.getPrice(), result.getPrice());
        Assertions.assertEquals(appleUpdateDto.getDescription(), result.getDescription());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void should_DeleteByIdProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void should_AddCategoryToProduct() {
        when(categoryService.findById(0L)).thenReturn(fruit);
        when(productRepository.findById(0L)).thenReturn(Optional.of(apple));
        when(productRepository.save(any(Product.class))).thenReturn(apple);

        productService.addCategoryToProduct(0L, 0L);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void should_RemoveCategoryFromProduct() {
        when(categoryService.findById(0L)).thenReturn(fruit);
        when(productRepository.findById(0L)).thenReturn(Optional.of(apple));
        when(productRepository.save(any(Product.class))).thenReturn(apple);

        productService.removeCategoryFromProduct(0L, 0L);

        verify(productRepository).save(any(Product.class));
    }
}
