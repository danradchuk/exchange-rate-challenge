package com.danradchuk.exchangeratechallenge.service;

import com.danradchuk.exchangeratechallenge.api.ExchangeRateClient;
import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import com.danradchuk.exchangeratechallenge.controller.ConversionResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    private final ExchangeRateClient client;

    public ExchangeRateService(ExchangeRateClient client) {
        this.client = client;
    }

    public ExchangeRateResponse getExchangeRates(@NotNull String from, @Nullable String to) {
        ExchangeRateResponse resp = client.fetchExchangeRates(from);
        Map<String, BigDecimal> rates = resp.getRates();
        if (to != null && rates.containsKey(to)) {
            return new ExchangeRateResponse(from, Map.of(to, rates.get(to)));
        }

        return resp;
    }

    public ConversionResponse convert(@NotNull String from, @Nullable String to, @NotNull BigDecimal amount) {
        ExchangeRateResponse resp = client.fetchExchangeRates(from);
        Map<String, BigDecimal> rates = resp.getRates();
        if (to != null && rates.containsKey(to)) {
            BigDecimal rate = rates.get(to);
            return new ConversionResponse(from, Map.of(to, amount.multiply(rate)));
        }

        Map<String, BigDecimal> result = new HashMap<>();
        rates.forEach((k, v) -> result.put(k, amount.multiply(v)));

        return new ConversionResponse(from, result);
    }
}
