package com.account.controller;

import com.account.config.AccountConfig;
import com.account.dto.Properties;
import com.account.model.Account;
import com.account.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountConfig config;
    private final AccountService accountService;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping
    public List<Account> getAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) {
        return accountService.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @GetMapping("/customer/{customerId}")
    public List<Account> getByCustomerId(@PathVariable Long customerId) {
        return accountService.findByCustomerId(customerId);
    }

    @GetMapping("/number/{accountNumber}")
    public Account getByAccountNumber(@PathVariable String accountNumber) {
        return accountService.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        return accountService.create(account);
    }

    @PutMapping("/{id}")
    public Account update(
            @PathVariable Long id,
            @RequestBody Account account
    ) {
        return accountService.update(id, account);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        accountService.delete(id);

        return "Account deleted successfully";
    }
    @GetMapping("/details/properties")
    public ResponseEntity<Properties> getProperties() {
        Properties properties = new Properties(
                config.getName(),
                config.getMsg(),
                config.getBuild(),
                config.getMailDetails(),
                config.getActiveBranches()

        );
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
    @GetMapping("/instance")
    public ResponseEntity<String> getServerInstance() {
        return ResponseEntity.ok("server is running on port" + serverPort);
    }


}