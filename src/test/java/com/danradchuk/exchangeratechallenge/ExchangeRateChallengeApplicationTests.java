package com.danradchuk.exchangeratechallenge;

import com.danradchuk.exchangeratechallenge.api.ExchangeRateApi;
import com.danradchuk.exchangeratechallenge.api.ExchangeRateClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ExchangeRateChallengeApplicationTests {
    @Autowired
    private ExchangeRateClient client;
    @Autowired
    private ExchangeRateApi api;
    @Value("${exchange-rate.api.url}")
    private String url;
    @Value("${exchange-rate.api.timeout.seconds}")
    private String timeout;

    @Test
    void contextLoads() {
        assertNotNull(client);
        assertNotNull(api);
        assertNotNull(url);
        assertNotNull(timeout);
    }

}
