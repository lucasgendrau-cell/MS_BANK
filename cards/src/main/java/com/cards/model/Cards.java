package com.cards.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//classe mappé dans une table de la base democards
@Entity
@Table(name = "cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cards {
    //crée la clé primaire
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private Long customerId;
    //la clé primaire ne peut pas être nul ni en double
    @Column(nullable = false, unique = true)
    private String cardNumber;

    private String cardType;

    private BigDecimal totalLimit;

    private BigDecimal amountUsed;

    private BigDecimal availableAmount;

    //créer et initialise à la date et heure actuel
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}