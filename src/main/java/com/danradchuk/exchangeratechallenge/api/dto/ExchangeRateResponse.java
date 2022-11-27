package com.danradchuk.exchangeratechallenge.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateResponse {
    private final String base;
    private final Map<String, BigDecimal> rates;

    @JsonCreator
    public ExchangeRateResponse(
            @JsonProperty("base") String base,
            @JsonProperty("rates") Map<String, BigDecimal> rates
    ) {
        this.base = base;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }
}
