package com.account.service;

import com.account.client.AccountClient;
import com.account.client.CardsClient;
import com.account.client.LoansClient;
import com.account.dto.CardsDTO;
import com.account.dto.CustomerFullResponse;
import com.account.dto.LoanDTO;
import com.account.model.Account;
import com.account.model.Customer;
import com.account.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFullService {

    private final CustomerRepository customerRepository;
    private final CardsClient cardsClient;
    private final LoansClient loansClient;
    private final AccountClient accountClient;

    public CustomerFullResponse getCustomerFull(Long customerId) {

        // 1️⃣ Customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2️⃣ Feign calls avec fallback safe
        List<Account> accounts;
        List<CardsDTO> cards;
        List<LoanDTO> loans;

        try {
            accounts = accountClient.getAccounts(customerId);
        } catch (Exception e) {
            accounts = List.of();
            e.printStackTrace();
        }

        try {
            cards = cardsClient.getCards(customerId);
        } catch (Exception e) {
            cards = List.of();
            e.printStackTrace();
        }

        try {
            loans = loansClient.getLoans(customerId);
        } catch (Exception e) {
            loans = List.of();
            e.printStackTrace();
        }

        // 3️⃣ Response final
        CustomerFullResponse response = new CustomerFullResponse();
        response.setCustomer(customer);
        response.setAccounts(accounts);
        response.setCards(cards);
        response.setLoans(loans);

        return response;
    }
}