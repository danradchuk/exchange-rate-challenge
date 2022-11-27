package com.danradchuk.exchangeratechallenge.api;

import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.micrometer.MicrometerCapability;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class ApiConfig {

    @Bean
    public ExchangeRateApi exchangeRateApi(
            @Value("${exchange-rate.api.url}") String url,
            @Value("${exchange-rate.api.timeout.seconds:60}") Integer timeout,
            MeterRegistry meterRegistry
    ) {
        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(CircuitBreaker.ofDefaults("exchange-rate-api"))
                .build();

        return Resilience4jFeign.builder(decorators)
                .options(new Request.Options(timeout, SECONDS, timeout, SECONDS, true))
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .errorDecoder(new ExchangeRateApiErrorDecoder())
                .addCapability(new MicrometerCapability(meterRegistry))
                .target(ExchangeRateApi.class, url);
    }
}
