package com.danradchuk.exchangeratechallenge.api;

import com.danradchuk.exchangeratechallenge.api.exception.ClientApiException;
import com.danradchuk.exchangeratechallenge.api.exception.ServerApiException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ExchangeRateApiErrorDecoder implements ErrorDecoder {
    static Default defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() < 500) {
            return new ClientApiException("Client error", response);
        } else if (response.status() >= 500) {
            return new ServerApiException("Server error", response);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
