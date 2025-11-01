package com.example.project2.service;

import com.example.project2.model.Flower;
import com.example.project2.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlowerService {

    @Autowired
    private FlowerRepository flowerRepository;

    public Page<Flower> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return flowerRepository.findByDeletedFalse(pageable);
        } else {
            return flowerRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public List<Flower> findAllActive() {
        return flowerRepository.findAll().stream().filter(f -> !f.isDeleted()).toList();
    }

    public Optional<Flower> findById(Long id) {
        return flowerRepository.findById(id);
    }

    public Flower save(Flower flower) {
        return flowerRepository.save(flower);
    }

    public void deleteLogical(Long id) {
        flowerRepository.findById(id).ifPresent(f -> {
            f.setDeleted(true);
            flowerRepository.save(f);
        });
    }

    public void restore(Long id) {
        flowerRepository.findById(id).ifPresent(f -> {
            f.setDeleted(false);
            flowerRepository.save(f);
        });
    }

    public Page<Flower> findAll(String keyword, String status, Pageable pageable) {
        if ("archived".equals(status)) {
            if (keyword == null || keyword.isEmpty()) {
                return flowerRepository.findByDeletedTrue(pageable);
            } else {
                return flowerRepository.findByDeletedTrueAndNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                return flowerRepository.findByDeletedFalse(pageable);
            } else {
                return flowerRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
            }
        }
    }

    public Page<Flower> findAll(String keyword, String status, Long categoryId, Long supplierId, Pageable pageable) {
        boolean isArchived = "archived".equals(status);
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasCategory = categoryId != null && categoryId > 0;
        boolean hasSupplier = supplierId != null && supplierId > 0;

        if (isArchived) {
            if (hasCategory && hasSupplier) {
                return hasKeyword
                    ? flowerRepository.findByDeletedTrueAndCategoryIdAndSupplierIdAndNameContainingIgnoreCase(categoryId, supplierId, keyword, pageable)
                    : flowerRepository.findByDeletedTrueAndCategoryIdAndSupplierId(categoryId, supplierId, pageable);
            } else if (hasCategory) {
                return hasKeyword
                    ? flowerRepository.findByDeletedTrueAndCategoryIdAndNameContainingIgnoreCase(categoryId, keyword, pageable)
                    : flowerRepository.findByDeletedTrueAndCategoryId(categoryId, pageable);
            } else if (hasSupplier) {
                return hasKeyword
                    ? flowerRepository.findByDeletedTrueAndSupplierIdAndNameContainingIgnoreCase(supplierId, keyword, pageable)
                    : flowerRepository.findByDeletedTrueAndSupplierId(supplierId, pageable);
            } else {
                return hasKeyword
                    ? flowerRepository.findByDeletedTrueAndNameContainingIgnoreCase(keyword, pageable)
                    : flowerRepository.findByDeletedTrue(pageable);
            }
        } else {
            if (hasCategory && hasSupplier) {
                return hasKeyword
                    ? flowerRepository.findByDeletedFalseAndCategoryIdAndSupplierIdAndNameContainingIgnoreCase(categoryId, supplierId, keyword, pageable)
                    : flowerRepository.findByDeletedFalseAndCategoryIdAndSupplierId(categoryId, supplierId, pageable);
            } else if (hasCategory) {
                return hasKeyword
                    ? flowerRepository.findByDeletedFalseAndCategoryIdAndNameContainingIgnoreCase(categoryId, keyword, pageable)
                    : flowerRepository.findByDeletedFalseAndCategoryId(categoryId, pageable);
            } else if (hasSupplier) {
                return hasKeyword
                    ? flowerRepository.findByDeletedFalseAndSupplierIdAndNameContainingIgnoreCase(supplierId, keyword, pageable)
                    : flowerRepository.findByDeletedFalseAndSupplierId(supplierId, pageable);
            } else {
                return hasKeyword
                    ? flowerRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable)
                    : flowerRepository.findByDeletedFalse(pageable);
            }
        }
    }


    public void deletePhysical(Long id) {
        flowerRepository.deleteById(id);
    }
}
