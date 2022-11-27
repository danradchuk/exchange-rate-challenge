package com.danradchuk.exchangeratechallenge.service;

import com.danradchuk.exchangeratechallenge.api.ExchangeRateClient;
import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRateService {
    private final ExchangeRateClient client;

    public ExchangeRateService(ExchangeRateClient client) {
        this.client = client;
    }

    public ExchangeRateResponse getExchangeRates(String from, String to) {
        ExchangeRateResponse resp = client.fetchExchangeRates(from);
        Map<String, BigDecimal> rates = resp.getRates();
        if (to != null && rates.containsKey(to)) {
            return new ExchangeRateResponse(from, Map.of(to, rates.get(to)));
        }

        return resp;
    }
}
