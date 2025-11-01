package com.example.project2.service;

import com.example.project2.model.Order;
import com.example.project2.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return orderRepository.findByDeletedFalse(pageable);
        } else {
            return orderRepository.findByDeletedFalseAndCustomerFullNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public Page<Order> findAll(String keyword, String status, Pageable pageable) {
        if ("archived".equals(status)) {
            if (keyword == null || keyword.isEmpty()) {
                return orderRepository.findByDeletedTrue(pageable);
            } else {
                return orderRepository.findByDeletedTrueAndCustomerFullNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                return orderRepository.findByDeletedFalse(pageable);
            } else {
                return orderRepository.findByDeletedFalseAndCustomerFullNameContainingIgnoreCase(keyword, pageable);
            }
        }
    }

    public List<Order> findAllActive() {
        return orderRepository.findAll().stream().filter(o -> !o.isDeleted()).toList();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void deleteLogical(Long id) {
        orderRepository.findById(id).ifPresent(o -> {
            o.setDeleted(true);
            orderRepository.save(o);
        });
    }

    public void restore(Long id) {
        orderRepository.findById(id).ifPresent(o -> {
            o.setDeleted(false);
            orderRepository.save(o);
        });
    }

    public void deletePhysical(Long id) {
        orderRepository.deleteById(id);
    }
}
