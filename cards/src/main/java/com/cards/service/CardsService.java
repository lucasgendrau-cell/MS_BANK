package com.cards.service;

import com.cards.model.Cards;
import com.cards.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardsService {

    private final CardsRepository cardRepository;

    public Cards create(Cards card) {
        return cardRepository.save(card);
    }

    public Cards update(Long cardId, Cards card) {

        Cards existing = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        existing.setCustomerId(card.getCustomerId());
        existing.setCardNumber(card.getCardNumber());
        existing.setCardType(card.getCardType());
        existing.setTotalLimit(card.getTotalLimit());
        existing.setAmountUsed(card.getAmountUsed());
        existing.setAvailableAmount(card.getAvailableAmount());

        return cardRepository.save(existing);
    }

    public void delete(Long cardId) {

        Cards existing = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        cardRepository.delete(existing);
    }
    //methode de lecture
    public Optional<Cards> findById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Cards> findByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId);
    }

    public Optional<Cards> findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    public List<Cards> findAll() {
        return cardRepository.findAll();
    }
}