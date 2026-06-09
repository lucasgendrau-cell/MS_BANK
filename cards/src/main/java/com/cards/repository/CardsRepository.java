package com.cards.repository;

import com.cards.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardsRepository extends JpaRepository<Cards, Long> {
    //liste les cartes d'un client
    List<Cards> findByCustomerId(Long customerId);
    // retourne carte du client si elle existe
    Optional<Cards> findByCardNumber(String cardNumber);
}