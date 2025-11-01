package com.example.project2.controller;

import com.example.project2.model.Order;
import com.example.project2.model.Flower;
import com.example.project2.model.Customer;
import com.example.project2.service.OrderService;
import com.example.project2.service.FlowerService;
import com.example.project2.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FlowerService flowerService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "orderDate") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(defaultValue = "active") String status) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

        Page<Order> orders = orderService.findAll(keyword, status, pageable);
        List<Customer> customers = customerService.findAllActive();
        List<Flower> flowers = flowerService.findAllActive();

        model.addAttribute("orders", orders);
        model.addAttribute("order", new Order());
        model.addAttribute("customers", customers);
        model.addAttribute("flowers", flowers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "orders";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Order order,
                       @RequestParam(required = false) Long customerId,
                       @RequestParam(required = false) List<Long> flowerIds) {
        if (customerId != null) {
            order.setCustomer(customerService.findById(customerId).orElse(null));
        }
        if (flowerIds != null && !flowerIds.isEmpty()) {
            List<Flower> selectedFlowers = flowerService.findAllActive().stream()
                    .filter(f -> flowerIds.contains(f.getId())).toList();
            order.setFlowers(selectedFlowers);
        }
        orderService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       @RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "orderDate") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDir,
                       @RequestParam(defaultValue = "active") String status) {
        
        Order order = orderService.findById(id).orElseThrow();
        
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        
        Page<Order> orders = orderService.findAll(keyword, status, pageable);
        model.addAttribute("orders", orders);
        model.addAttribute("order", order);
        model.addAttribute("customers", customerService.findAllActive());
        model.addAttribute("flowers", flowerService.findAllActive());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "orders";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue="0") int page,
                          @RequestParam(defaultValue="active") String status) {
        orderService.deleteLogical(id);
        StringBuilder redirect = new StringBuilder("redirect:/orders?page=").append(page).append("&status=").append(status);
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
        orderService.restore(id);
        StringBuilder redirect = new StringBuilder("redirect:/orders?page=").append(page).append("&status=").append(status);
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
        orderService.deletePhysical(id);
        StringBuilder redirect = new StringBuilder("redirect:/orders?page=").append(page).append("&status=").append(status);
        if (keyword != null && !keyword.isEmpty()) {
            redirect.append("&keyword=").append(keyword);
        }
        return redirect.toString();
    }
}
