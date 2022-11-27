package com.danradchuk.exchangeratechallenge.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int code;
    private final String error;
    private final String details;

    @JsonCreator
    public ErrorResponse(
            @JsonProperty("code") int code,
            @JsonProperty("error") String error,
            @JsonProperty("details") String details
    ) {
        this.code = code;
        this.error = error;
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getDetails() {
        return details;
    }
}
