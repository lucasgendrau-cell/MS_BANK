package com.account.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long customerId;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    private String accountType;

    private String bankAddress;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
}