package com.danradchuk.exchangeratechallenge.controller;

import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import com.danradchuk.exchangeratechallenge.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v0/exchange-rates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Operation(summary = "Fetch the latest foreign exchange reference rates")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExchangeRateResponse.class)
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ExchangeRateResponse> getExchangeRates(
            @Parameter(description = "Base currency") @RequestParam("from") String from,
            @Parameter(description = "Output currency") @RequestParam(value = "to", required = false) String to
    ) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRates(from, to));
    }

    @Operation(summary = "Convert any amount from one currency to another")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConversionResponse.class)
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/converter")
    public ResponseEntity<ConversionResponse> convert(
            @Parameter(description = "Currency to convert from") @RequestParam("from") String from,
            @Parameter(description = "Currency to convert to") @RequestParam(value = "to", required = false) String to,
            @Parameter(description = "The amount to be converted") @RequestParam("amount") BigDecimal amount
    ) {
        return ResponseEntity.ok(exchangeRateService.convert(from, to, amount));
    }
}
