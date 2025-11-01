package com.example.project2.repository;

import com.example.project2.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByDeletedFalse(Pageable pageable);
    Page<Category> findByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Category> findByDeletedTrue(Pageable pageable);
    Page<Category> findByDeletedTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}
