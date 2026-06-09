package com.account.client;

import com.account.dto.LoanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "loans")
public interface LoansClient {

    @GetMapping("/api/v1/loans/customer/{customerId}")
    List<LoanDTO> getLoans(@PathVariable Long customerId);
}