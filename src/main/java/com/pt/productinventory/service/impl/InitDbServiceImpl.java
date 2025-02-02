package com.pt.productinventory.service.impl;

import com.pt.productinventory.model.Category;
import com.pt.productinventory.model.Product;
import com.pt.productinventory.repository.CategoryRepository;
import com.pt.productinventory.service.InitDbService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InitDbServiceImpl implements InitDbService {

    private final CategoryRepository categoryRepository;
    private final String className = this.getClass().getSimpleName();

    @PostConstruct
    @Override
    public void initDatabase() {
        log.debug("calling initDatabase method in {}", className);

        Category fruit = Category.builder()
                .name("fruit")
                .build();
        Product apple = Product.builder()
                .name("Apple")
                .price(25.99)
                .quantity(15)
                .description("This is a non GMO apple!")
                .category(fruit)
                .build();
        Product pear = Product.builder()
                .name("Pear")
                .price(35.00)
                .quantity(10)
                .description("This is a non GMO pear!")
                .category(fruit)
                .build();
        fruit.getProducts().addAll(List.of(apple, pear));

        Category hardware = Category.builder()
                .name("hardware")
                .build();
        Product wirelessHeadphones = Product.builder()
                .name("Wireless Headphones")
                .price(49.99)
                .quantity(1)
                .description("Bluetooth wireless headphones!")
                .category(hardware)
                .build();
        hardware.getProducts().add(wirelessHeadphones);

        //        productRepository.saveAll(List.of(apple, pear, wirelessHeadphones));
//        categoryRepository.saveAll(List.of(fruit, hardware));
    }
}
