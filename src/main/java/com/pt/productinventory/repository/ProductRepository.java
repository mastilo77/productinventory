package com.pt.productinventory.repository;

import com.pt.productinventory.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Product> S save(S entity);

    @Override
    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Product> List<S> saveAll(Iterable<S> entities);
}
