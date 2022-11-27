package com.danradchuk.exchangeratechallenge.controller;

import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import com.danradchuk.exchangeratechallenge.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v0/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<ExchangeRateResponse> getExchangeRates(
            @RequestParam("from") String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRates(from, to));
    }

    @GetMapping("/converter")
    public ResponseEntity<ConversionResponse> convert(
            @RequestParam("from") String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam("amount") BigDecimal amount
    ) {
        return ResponseEntity.ok(exchangeRateService.convert(from, to, amount));
    }
}
