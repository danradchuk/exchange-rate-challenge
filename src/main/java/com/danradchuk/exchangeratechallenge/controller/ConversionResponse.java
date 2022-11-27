package com.danradchuk.exchangeratechallenge.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversionResponse {
    private final String from;
    private final Map<String, BigDecimal> result;

    @JsonCreator
    public ConversionResponse(
            @JsonProperty("from") String from,
            @JsonProperty("result") Map<String, BigDecimal> result
    ) {
        this.from = from;
        this.result = result;
    }

    public String getFrom() {
        return from;
    }

    public Map<String, BigDecimal> getResult() {
        return result;
    }
}
