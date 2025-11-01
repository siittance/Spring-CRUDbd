package com.example.project2.service;

import com.example.project2.model.Category;
import com.example.project2.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return categoryRepository.findByDeletedFalse(pageable);
        } else {
            return categoryRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public Page<Category> findAll(String keyword, String status, Pageable pageable) {
        if ("archived".equals(status)) {
            if (keyword == null || keyword.isEmpty()) {
                return categoryRepository.findByDeletedTrue(pageable);
            } else {
                return categoryRepository.findByDeletedTrueAndNameContainingIgnoreCase(keyword, pageable);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                return categoryRepository.findByDeletedFalse(pageable);
            } else {
                return categoryRepository.findByDeletedFalseAndNameContainingIgnoreCase(keyword, pageable);
            }
        }
    }

    public List<Category> findAllActive() {
        return categoryRepository.findAll().stream().filter(c -> !c.isDeleted()).toList();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteLogical(Long id) {
        categoryRepository.findById(id).ifPresent(c -> {
            c.setDeleted(true);
            categoryRepository.save(c);
        });
    }

    public void restore(Long id) {
        categoryRepository.findById(id).ifPresent(c -> {
            c.setDeleted(false);
            categoryRepository.save(c);
        });
    }

    public void deletePhysical(Long id) {
        categoryRepository.deleteById(id);
    }
}
