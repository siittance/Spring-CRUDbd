package com.example.project2.controller;

import com.example.project2.model.Customer;
import com.example.project2.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(defaultValue="fullName") String sortField,
                       @RequestParam(defaultValue="asc") String sortDir,
                       @RequestParam(defaultValue="active") String status) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        Page<Customer> customers = customerService.findAll(keyword, status, pageable);
        model.addAttribute("customers", customers);
        model.addAttribute("customer", new Customer());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "customers";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(defaultValue="fullName") String sortField,
                       @RequestParam(defaultValue="asc") String sortDir,
                       @RequestParam(defaultValue="active") String status) {
        
        Customer customer = customerService.findById(id).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        
        Page<Customer> customers = customerService.findAll(keyword, status, pageable);
        model.addAttribute("customers", customers);
        model.addAttribute("customer", customer);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue="0") int page,
                         @RequestParam(defaultValue="active") String status) {
        customerService.deleteLogical(id);
        StringBuilder redirect = new StringBuilder("redirect:/customers?page=").append(page).append("&status=").append(status);
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
        customerService.restore(id);
        StringBuilder redirect = new StringBuilder("redirect:/customers?page=").append(page).append("&status=").append(status);
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
        customerService.deletePhysical(id);
        StringBuilder redirect = new StringBuilder("redirect:/customers?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        return redirect.toString();
    }
}
