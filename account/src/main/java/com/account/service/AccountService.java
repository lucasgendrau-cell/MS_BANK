package com.account.service;

import com.account.model.Account;
import com.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public Account update(Long accountId, Account account) {

        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        existing.setCustomerId(account.getCustomerId());
        existing.setAccountNumber(account.getAccountNumber());
        existing.setAccountType(account.getAccountType());
        existing.setBankAddress(account.getBankAddress());

        return accountRepository.save(existing);
    }

    public void delete(Long accountId) {

        Account existing = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.delete(existing);
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Account> findByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}