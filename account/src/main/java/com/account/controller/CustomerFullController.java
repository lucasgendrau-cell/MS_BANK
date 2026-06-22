package com.account.controller;

import com.account.dto.ClientResponse;
import com.account.dto.CustomerFullResponse;
import com.account.service.CustomerFullService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerFullController {

    private final CustomerFullService customerFullService;

    @GetMapping("/full/{customerId}")
    public ClientResponse<CustomerFullResponse> getCustomerFull(
            @PathVariable Long customerId) {

        System.out.println("=== CALL /full/" + customerId + " ===");

        ClientResponse<CustomerFullResponse> response =
                customerFullService.getCustomerFull(customerId);

        System.out.println("=== RESPONSE READY ===");

        return response;
    }
}