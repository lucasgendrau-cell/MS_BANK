package com.account.service;

import com.account.client.AccountClient;
import com.account.client.CardsClient;
import com.account.client.LoansClient;
import com.account.dto.CardsDTO;
import com.account.dto.ClientResponse;
import com.account.dto.CustomerFullResponse;
import com.account.dto.LoanDTO;
import com.account.model.Customer;
import com.account.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerFullService {

    private static final String CUSTOMER_NOT_FOUND = "Client introuvable";

    private static final String CUSTOMER_DETAILS_SERVICE = "customerDetailsService";
    private static final String CUSTOMER_DETAILS_RETRY = "customerDetailsRetry";
    private static final String CUSTOMER_DETAILS_FALLBACK = "getCustomerFullFallback";

    private static final String CARDS_RATE_LIMITER = "cardsServiceRateLimiter";
    private static final String LOANS_RATE_LIMITER = "loansServiceRateLimiter";

    private static final Logger LOG =
            LoggerFactory.getLogger(CustomerFullService.class);

    private final CustomerRepository customerRepository;
    private final CardsClient cardsClient;
    private final LoansClient loansClient;
    private final AccountClient accountClient;

    @Retry(
            name = CUSTOMER_DETAILS_RETRY,
            fallbackMethod = CUSTOMER_DETAILS_FALLBACK
    )
    @CircuitBreaker(
            name = CUSTOMER_DETAILS_SERVICE,
            fallbackMethod = CUSTOMER_DETAILS_FALLBACK
    )
    public ClientResponse<CustomerFullResponse> getCustomerFull(Long customerId) {

        LOG.info("\n\n====================================================");
        LOG.info("🚀 DÉBUT CUSTOMER FULL - ID {}", customerId);
        LOG.info("====================================================\n");

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    LOG.error("\n❌ CLIENT INTROUVABLE - ID {}\n", customerId);
                    return new RuntimeException(CUSTOMER_NOT_FOUND);
                });

        LOG.info("\n✅ CLIENT TROUVÉ : {}\n", customer.getCustomerId());

        boolean allServicesAvailable = true;

        // ================= ACCOUNTS =================
        List<?> accounts;
        try {
            LOG.info("\n📡 [ACCOUNTS] Appel microservice...\n");
            accounts = accountClient.getAccounts(customerId);
            LOG.info("\n✅ [ACCOUNTS] OK - {} comptes récupérés\n", accounts.size());
        } catch (Exception e) {
            LOG.error("\n❌ [ACCOUNTS] KO : {}\n", e.getMessage());
            accounts = List.of();
            allServicesAvailable = false;
        }

        // ================= CARDS =================
        List<CardsDTO> cards;
        try {
            LOG.info("\n📡 [CARDS] Appel microservice...\n");
            cards = getCardsWithRateLimit(customerId);
            LOG.info("\n✅ [CARDS] OK - {} cartes récupérées\n", cards.size());

            if (cards.isEmpty()) {
                allServicesAvailable = false;
            }

        } catch (Exception e) {
            LOG.error("\n❌ [CARDS] KO : {}\n", e.getMessage());
            cards = List.of();
            allServicesAvailable = false;
        }

        // ================= LOANS =================
        List<LoanDTO> loans;
        try {
            LOG.info("\n📡 [LOANS] Appel microservice...\n");
            loans = getLoansWithRateLimit(customerId);
            LOG.info("\n✅ [LOANS] OK - {} prêts récupérés\n", loans.size());

            if (loans.isEmpty()) {
                allServicesAvailable = false;
            }

        } catch (Exception e) {
            LOG.error("\n❌ [LOANS] KO : {}\n", e.getMessage());
            loans = List.of();
            allServicesAvailable = false;
        }

        // ================= RESPONSE =================
        CustomerFullResponse response = new CustomerFullResponse();
        response.setCustomer(customer);
        response.setAccounts((List) accounts);
        response.setCards(cards);
        response.setLoans(loans);
        response.setMessage(allServicesAvailable
                ? "Tous les microservices sont disponibles"
                : "Un ou plusieurs microservices sont indisponibles");

        if (allServicesAvailable) {
            LOG.info("\n🎯 SUCCESS CUSTOMER FULL ID {}", customerId);
        } else {
            LOG.warn("\n🟥 PARTIAL RESPONSE CUSTOMER FULL ID {}", customerId);
        }

        LOG.info("====================================================\n\n");

        return ClientResponse.<CustomerFullResponse>builder()
                .data(response)
                .serviceAvailable(allServicesAvailable)
                .message(response.getMessage())
                .build();
    }

    // ================= CARDS RATE LIMITER =================

    @RateLimiter(
            name = CARDS_RATE_LIMITER,
            fallbackMethod = "cardsRateLimiterFallback"
    )
    public List<CardsDTO> getCardsWithRateLimit(Long customerId) {
        return cardsClient.getCards(customerId);
    }

    private List<CardsDTO> cardsRateLimiterFallback(
            Long customerId,
            Throwable throwable) {

        LOG.warn("\n🟡 [CARDS] RATE LIMITER ACTIVÉ - Trop de requêtes\n");

        return List.of();
    }

    // ================= LOANS RATE LIMITER =================

    @RateLimiter(
            name = LOANS_RATE_LIMITER,
            fallbackMethod = "loansRateLimiterFallback"
    )
    public List<LoanDTO> getLoansWithRateLimit(Long customerId) {
        return loansClient.getLoans(customerId);
    }

    private List<LoanDTO> loansRateLimiterFallback(
            Long customerId,
            Throwable throwable) {

        LOG.warn("\n🟡 [LOANS] RATE LIMITER ACTIVÉ - Trop de requêtes\n");

        return List.of();
    }

    // ================= GLOBAL FALLBACK =================

    private ClientResponse<CustomerFullResponse> getCustomerFullFallback(
            Long customerId,
            Throwable throwable) {

        LOG.warn("\n\n====================================================");
        LOG.warn("⚠ FALLBACK ACTIVÉ - ID {}", customerId);
        LOG.warn("⚠ CAUSE : {}", throwable.getMessage());
        LOG.warn("====================================================\n\n");

        Customer customer = customerRepository.findById(customerId)
                .orElse(null);

        CustomerFullResponse response = new CustomerFullResponse();
        response.setCustomer(customer);
        response.setAccounts(List.of());
        response.setCards(List.of());
        response.setLoans(List.of());
        response.setMessage("Réponse partielle (fallback activé)");

        return ClientResponse.<CustomerFullResponse>builder()
                .data(response)
                .serviceAvailable(false)
                .message("Service indisponible - fallback activé")
                .build();
    }
}