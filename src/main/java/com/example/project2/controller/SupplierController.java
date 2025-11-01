package com.example.project2.controller;

import com.example.project2.model.Supplier;
import com.example.project2.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(defaultValue="name") String sortField,
                       @RequestParam(defaultValue="asc") String sortDir,
                       @RequestParam(defaultValue="active") String status) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        Page<Supplier> suppliers = supplierService.findAll(keyword, status, pageable);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "suppliers";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Supplier supplier) {
        supplierService.save(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(defaultValue="name") String sortField,
                       @RequestParam(defaultValue="asc") String sortDir,
                       @RequestParam(defaultValue="active") String status) {
        
        Supplier supplier = supplierService.findById(id).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        
        Page<Supplier> suppliers = supplierService.findAll(keyword, status, pageable);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplier", supplier);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "suppliers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue="0") int page,
                         @RequestParam(defaultValue="active") String status) {
        supplierService.deleteLogical(id);
        StringBuilder redirect = new StringBuilder("redirect:/suppliers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        return redirect.toString();
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue="0") int page,
                          @RequestParam(defaultValue="archived") String status) {
        supplierService.restore(id);
        StringBuilder redirect = new StringBuilder("redirect:/suppliers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        return redirect.toString();
    }

    @GetMapping("/delete-physical/{id}")
    public String deletePhysical(@PathVariable Long id,
                                 @RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="active") String status) {
        supplierService.deletePhysical(id);
        StringBuilder redirect = new StringBuilder("redirect:/suppliers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        return redirect.toString();
    }
}
