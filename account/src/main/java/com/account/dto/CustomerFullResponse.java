package com.account.dto;

import com.account.model.Account;
import com.account.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFullResponse {

    private String message;

    private Customer customer;

    private List<Account> accounts;

    private List<CardsDTO> cards;

    private List<LoanDTO> loans;
}