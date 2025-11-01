package com.example.project2.repository;

import com.example.project2.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByDeletedFalse(Pageable pageable);

    Page<Customer> findByDeletedFalseAndFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    
    Page<Customer> findByDeletedTrue(Pageable pageable);
    
    Page<Customer> findByDeletedTrueAndFullNameContainingIgnoreCase(String fullName, Pageable pageable);
}
