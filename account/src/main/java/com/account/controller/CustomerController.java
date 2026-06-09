package com.account.controller;

import com.account.model.Customer;
import com.account.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return customerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @GetMapping("/email/{email}")
    public Customer getByEmail(@PathVariable String email) {
        return customerService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @GetMapping("/mobile/{mobile}")
    public Customer getByMobile(@PathVariable String mobile) {
        return customerService.findByMobileNumber(mobile)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public Customer update(
            @PathVariable Long id,
            @RequestBody Customer customer
    ) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        customerService.delete(id);

        return "Customer deleted successfully";
    }
}