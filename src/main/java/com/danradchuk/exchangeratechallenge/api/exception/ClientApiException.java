package com.danradchuk.exchangeratechallenge.api.exception;

import feign.Response;

public class ClientApiException extends RuntimeException {
    private final Response response;

    public ClientApiException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
