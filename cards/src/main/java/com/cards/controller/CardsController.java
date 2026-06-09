package com.cards.controller;

import com.cards.model.Cards;
import com.cards.service.CardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// API REST
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardsController {

    private final CardsService cardsService;
    //Recupère liste de toutes les cartes
    @GetMapping
    public List<Cards> getAll() {
        return cardsService.findAll();
    }
    //Recupère carte par son ID
    @GetMapping("/{id}")
    public Cards getById(@PathVariable Long id) {
        return cardsService.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }
    //récupère toute les cartes associés à un client
    @GetMapping("/customer/{customerId}")
    public List<Cards> getByCustomerId(@PathVariable Long customerId) {
        return cardsService.findByCustomerId(customerId);
    }
    //affiche toute les informations d'une carte bancaire à partir de son numéro
    @GetMapping("/number/{cardNumber}")
    public Cards getByCardNumber(@PathVariable String cardNumber) {
        return cardsService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }
    //créer une nouvelle carte
    @PostMapping
    public Cards create(@RequestBody Cards card) {
        return cardsService.create(card);
    }
    //Mets à jour une carte bancaire à partir de l'id de la carte
    @PutMapping("/{id}")
    public Cards update(
            @PathVariable Long id,
            @RequestBody Cards card
    ) {
        return cardsService.update(id, card);
    }
    //Supprimer une carte bancaire
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        cardsService.delete(id);

        return "Card deleted successfully";
    }
}