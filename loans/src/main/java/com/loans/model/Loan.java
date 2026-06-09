package com.loans.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private Long customerId;

    @Column(nullable = false, unique = true)
    private String loanNumber;

    private String loanType;

    private BigDecimal totalLoan;

    private BigDecimal amountPaid;

    private BigDecimal outstandingAmount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime startDate;
}