package com.account.client;

import com.account.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "account")
public interface AccountClient {

    @GetMapping("/api/v1/account/customer/{customerId}")
    List<Account> getAccounts(@PathVariable Long customerId);
}