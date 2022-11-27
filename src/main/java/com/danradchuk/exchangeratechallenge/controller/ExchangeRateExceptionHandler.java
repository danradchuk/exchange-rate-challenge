package com.danradchuk.exchangeratechallenge.controller;

import com.danradchuk.exchangeratechallenge.api.exception.ClientApiException;
import com.danradchuk.exchangeratechallenge.api.exception.ServerApiException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExchangeRateExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("exchange-rate-error-logger");

    @ExceptionHandler(value = ClientApiException.class)
    public ResponseEntity<ErrorResponse> handleClientExceptions(ClientApiException e) {
        logger.error("{}", e.getResponse().request().toString());
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        BAD_REQUEST.value(),
                        "Bad Request",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(value = ServerApiException.class)
    public ResponseEntity<ErrorResponse> handleServerExceptions(ServerApiException e) {
        logger.error("{}", e.getResponse().request().toString());
        return ResponseEntity.internalServerError().body(
                new ErrorResponse(
                        INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        e.getMessage()
                )
        );
    }

    @ExceptionHandler(value = CallNotPermittedException.class)
    public ResponseEntity<ErrorResponse> handleCircuitBreakerException(CallNotPermittedException e) {
        logger.error("{}", e.getMessage());
        return ResponseEntity.internalServerError().body(
                new ErrorResponse(
                        SERVICE_UNAVAILABLE.value(),
                        "Service temporarily unavailable",
                        e.getMessage()
                )
        );
    }
}
