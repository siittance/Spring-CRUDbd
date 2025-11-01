package com.example.project2.service;

import com.example.project2.model.Supplier;
import com.example.project2.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Page<Supplier> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return supplierRepository.findByDeletedFalse(pageable);
        } else {
            return supplierRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public Page<Supplier> findAll(String keyword, String status, Pageable pageable) {
        if ("archived".equals(status)) {
            if (keyword == null || keyword.isEmpty()) {
                return supplierRepository.findByDeletedTrue(pageable);
            } else {
                return supplierRepository.findByDeletedTrueAndNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                return supplierRepository.findByDeletedFalse(pageable);
            } else {
                return supplierRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
            }
        }
    }

    public List<Supplier> findAllActive() {
        return supplierRepository.findAll().stream().filter(s -> !s.isDeleted()).toList();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void deleteLogical(Long id) {
        supplierRepository.findById(id).ifPresent(s -> {
            s.setDeleted(true);
            supplierRepository.save(s);
        });
    }

    public void restore(Long id) {
        supplierRepository.findById(id).ifPresent(s -> {
            s.setDeleted(false);
            supplierRepository.save(s);
        });
    }

    public void deletePhysical(Long id) {
        supplierRepository.deleteById(id);
    }
}
