package com.example.project2.repository;

import com.example.project2.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Page<Supplier> findByDeletedFalse(Pageable pageable);
    Page<Supplier> findByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Supplier> findByDeletedTrue(Pageable pageable);
    Page<Supplier> findByDeletedTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}
