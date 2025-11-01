package com.example.project2.controller;

import com.example.project2.model.Category;
import com.example.project2.model.Flower;
import com.example.project2.model.Supplier;
import com.example.project2.service.CategoryService;
import com.example.project2.service.FlowerService;
import com.example.project2.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flowers")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    // список с пагинацией, поиском и фильтром по статусу
    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(defaultValue = "active") String status,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) Long supplierId) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        Page<Flower> flowers = flowerService.findAll(keyword, status, categoryId, supplierId, pageable);

        model.addAttribute("flowers", flowers);
        model.addAttribute("flower", new Flower());
        model.addAttribute("categories", categoryService.findAll("", Pageable.unpaged()).getContent());
        model.addAttribute("suppliers", supplierService.findAll("", Pageable.unpaged()).getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "flowers";
    }

    // добавление и редактирование
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Flower flower,
                       BindingResult result,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) Long supplierId) {

        if (result.hasErrors()) {
            return "flowers";
        }

        // устанавливаем категории и поставщиков по id
        if (categoryId != null) {
            flower.setCategory(categoryService.findById(categoryId).orElse(null));
        }
        if (supplierId != null) {
            flower.setSupplier(supplierService.findById(supplierId).orElse(null));
        }

        flowerService.save(flower);
        return "redirect:/flowers";
    }

    // редактирование
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(defaultValue = "active") String status,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) Long supplierId) {
        
        Flower flower = flowerService.findById(id).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        
        Page<Flower> flowers = flowerService.findAll(keyword, status, categoryId, supplierId, pageable);
        model.addAttribute("flowers", flowers);
        model.addAttribute("flower", flower);
        model.addAttribute("categories", categoryService.findAll("", Pageable.unpaged()).getContent());
        model.addAttribute("suppliers", supplierService.findAll("", Pageable.unpaged()).getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "flowers";
    }

    // логическое удаление
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue="0") int page,
                         @RequestParam(required = false) Long categoryId,
                         @RequestParam(required = false) Long supplierId,
                         @RequestParam(defaultValue="active") String status) {
        flowerService.deleteLogical(id);
        StringBuilder redirect = new StringBuilder("redirect:/flowers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        if (categoryId != null) {
            redirect.append("&categoryId=").append(categoryId);
        }
        if (supplierId != null) {
            redirect.append("&supplierId=").append(supplierId);
        }
        return redirect.toString();
    }

    // восстановление
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue="0") int page,
                          @RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) Long supplierId,
                          @RequestParam(defaultValue="archived") String status) {
        flowerService.restore(id);
        StringBuilder redirect = new StringBuilder("redirect:/flowers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        if (categoryId != null) {
            redirect.append("&categoryId=").append(categoryId);
        }
        if (supplierId != null) {
            redirect.append("&supplierId=").append(supplierId);
        }
        return redirect.toString();
    }

    // физическое удаление
    @GetMapping("/delete-physical/{id}")
    public String deletePhysical(@PathVariable Long id,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue="0") int page,
                                 @RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) Long supplierId,
                                 @RequestParam(defaultValue="active") String status) {
        flowerService.deletePhysical(id);
        StringBuilder redirect = new StringBuilder("redirect:/flowers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        if (categoryId != null) {
            redirect.append("&categoryId=").append(categoryId);
        }
        if (supplierId != null) {
            redirect.append("&supplierId=").append(supplierId);
        }
        return redirect.toString();
    }
}
