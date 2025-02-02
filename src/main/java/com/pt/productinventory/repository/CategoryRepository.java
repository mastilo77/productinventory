package com.pt.productinventory.repository;

import com.pt.productinventory.model.Category;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Override
    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Category> S save(S entity);

    @Override
    @Lock(value = LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Category> List<S> saveAll(Iterable<S> entities);
}
