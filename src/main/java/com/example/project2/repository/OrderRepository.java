package com.example.project2.repository;

import com.example.project2.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByDeletedFalse(Pageable pageable);
    Page<Order> findByDeletedFalseAndCustomerFullNameContainingIgnoreCase(String customerName, Pageable pageable);
    Page<Order> findByDeletedTrue(Pageable pageable);
    Page<Order> findByDeletedTrueAndCustomerFullNameContainingIgnoreCase(String customerName, Pageable pageable);
}
