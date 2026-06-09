package com.account.controller;

import com.account.model.Account;
import com.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

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
}