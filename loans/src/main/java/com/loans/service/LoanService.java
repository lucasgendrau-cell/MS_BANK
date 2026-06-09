package com.loans.service;

import com.loans.model.Loan;
import com.loans.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public Loan create(Loan loan) {

        if (loan.getTotalLoan() != null && loan.getAmountPaid() != null) {
            loan.setOutstandingAmount(
                    loan.getTotalLoan().subtract(loan.getAmountPaid())
            );
        }

        return loanRepository.save(loan);
    }

    public Loan update(Long loanId, Loan loan) {

        Loan existing = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        existing.setCustomerId(loan.getCustomerId());
        existing.setLoanNumber(loan.getLoanNumber());
        existing.setLoanType(loan.getLoanType());
        existing.setTotalLoan(loan.getTotalLoan());
        existing.setAmountPaid(loan.getAmountPaid());

        if (loan.getTotalLoan() != null && loan.getAmountPaid() != null) {
            existing.setOutstandingAmount(
                    loan.getTotalLoan().subtract(loan.getAmountPaid())
            );
        }

        return loanRepository.save(existing);
    }

    public void delete(Long loanId) {

        Loan existing = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loanRepository.delete(existing);
    }

    public Optional<Loan> findById(Long loanId) {
        return loanRepository.findById(loanId);
    }

    public List<Loan> findByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId);
    }

    public Optional<Loan> findByLoanNumber(String loanNumber) {
        return loanRepository.findByLoanNumber(loanNumber);
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }
}