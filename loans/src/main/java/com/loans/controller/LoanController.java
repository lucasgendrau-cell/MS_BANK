package com.loans.controller;

import com.loans.model.Loan;
import com.loans.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public List<Loan> getAll() {
        return loanService.findAll();
    }

    @GetMapping("/{id}")
    public Loan getById(@PathVariable Long id) {
        return loanService.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @GetMapping("/customer/{customerId}")
    public List<Loan> getByCustomerId(@PathVariable Long customerId) {
        return loanService.findByCustomerId(customerId);
    }

    @GetMapping("/number/{loanNumber}")
    public Loan getByLoanNumber(@PathVariable String loanNumber) {
        return loanService.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @PostMapping
    public Loan create(@RequestBody Loan loan) {
        return loanService.create(loan);
    }

    @PutMapping("/{id}")
    public Loan update(
            @PathVariable Long id,
            @RequestBody Loan loan
    ) {
        return loanService.update(id, loan);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        loanService.delete(id);

        return "Loan deleted successfully";
    }
}