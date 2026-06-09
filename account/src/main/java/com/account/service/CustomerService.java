package com.account.service;

import com.account.model.Customer;
import com.account.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Long customerId, Customer customer) {

        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setMobileNumber(customer.getMobileNumber());

        return customerRepository.save(existing);
    }

    public void delete(Long customerId) {

        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepository.delete(existing);
    }

    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Optional<Customer> findByMobileNumber(String mobileNumber) {
        return customerRepository.findByMobileNumber(mobileNumber);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}