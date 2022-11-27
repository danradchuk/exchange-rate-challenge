package com.danradchuk.exchangeratechallenge.api;

import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExchangeRateClient {
    private final ExchangeRateApi api;

    public ExchangeRateClient(ExchangeRateApi api) {
        this.api = api;
    }

    public ExchangeRateResponse fetchExchangeRates(@NotNull String from) {
        return api.fetchExchangeRates(Map.of("base", from));
    }
}
