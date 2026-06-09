package com.account.client;

import com.account.dto.CardsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cards")
public interface CardsClient {

    @GetMapping("/api/v1/cards/customer/{customerId}")
    List<CardsDTO> getCards(@PathVariable Long customerId);
}