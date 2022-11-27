package com.danradchuk.exchangeratechallenge.api;

import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface ExchangeRateApi {
    @RequestLine("GET /latest")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    ExchangeRateResponse fetchExchangeRates(@QueryMap Map<String, Object> params);

}
