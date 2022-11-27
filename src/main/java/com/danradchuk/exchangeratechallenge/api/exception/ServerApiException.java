package com.danradchuk.exchangeratechallenge.api.exception;

import feign.Response;

public class ServerApiException extends RuntimeException {
    private final Response response;

    public ServerApiException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
