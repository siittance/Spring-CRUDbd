package com.example.project2.repository;

import com.example.project2.model.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
    Page<Flower> findByDeletedFalse(Pageable pageable);
    Page<Flower> findByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Flower> findByDeletedTrue(Pageable pageable);
    Page<Flower> findByDeletedTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
    
    // фильтрация по категории
    Page<Flower> findByDeletedFalseAndCategoryId(Long categoryId, Pageable pageable);
    Page<Flower> findByDeletedFalseAndCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name, Pageable pageable);
    Page<Flower> findByDeletedTrueAndCategoryId(Long categoryId, Pageable pageable);
    Page<Flower> findByDeletedTrueAndCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name, Pageable pageable);
    
    // фильтрация по поставщику
    Page<Flower> findByDeletedFalseAndSupplierId(Long supplierId, Pageable pageable);
    Page<Flower> findByDeletedFalseAndSupplierIdAndNameContainingIgnoreCase(Long supplierId, String name, Pageable pageable);
    Page<Flower> findByDeletedTrueAndSupplierId(Long supplierId, Pageable pageable);
    Page<Flower> findByDeletedTrueAndSupplierIdAndNameContainingIgnoreCase(Long supplierId, String name, Pageable pageable);
    
    // фильтрация по категории и поставщику
    Page<Flower> findByDeletedFalseAndCategoryIdAndSupplierId(Long categoryId, Long supplierId, Pageable pageable);
    Page<Flower> findByDeletedFalseAndCategoryIdAndSupplierIdAndNameContainingIgnoreCase(Long categoryId, Long supplierId, String name, Pageable pageable);
    Page<Flower> findByDeletedTrueAndCategoryIdAndSupplierId(Long categoryId, Long supplierId, Pageable pageable);
    Page<Flower> findByDeletedTrueAndCategoryIdAndSupplierIdAndNameContainingIgnoreCase(Long categoryId, Long supplierId, String name, Pageable pageable);
}
