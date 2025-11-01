package com.example.project2.service;

import com.example.project2.model.Customer;
import com.example.project2.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return customerRepository.findByDeletedFalse(pageable);
        } else {
            return customerRepository.findByDeletedFalseAndFullNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public Page<Customer> findAll(String keyword, String status, Pageable pageable) {
        if ("archived".equals(status)) {
            if (keyword == null || keyword.isEmpty()) {
                return customerRepository.findByDeletedTrue(pageable);
            } else {
                return customerRepository.findByDeletedTrueAndFullNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                return customerRepository.findByDeletedFalse(pageable);
            } else {
                return customerRepository.findByDeletedFalseAndFullNameContainingIgnoreCase(keyword, pageable);
            }
        }
    }

    public List<Customer> findAllActive() {
        return customerRepository.findAll().stream().filter(c -> !c.isDeleted()).toList();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteLogical(Long id) {
        customerRepository.findById(id).ifPresent(c -> {
            c.setDeleted(true);
            customerRepository.save(c);
        });
    }

    public void restore(Long id) {
        customerRepository.findById(id).ifPresent(c -> {
            c.setDeleted(false);
            customerRepository.save(c);
        });
    }

    public void deletePhysical(Long id) {
        customerRepository.deleteById(id);
    }
}
