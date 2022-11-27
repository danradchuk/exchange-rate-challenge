package com.danradchuk.exchangeratechallenge;

import com.danradchuk.exchangeratechallenge.api.ExchangeRateApi;
import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import com.danradchuk.exchangeratechallenge.controller.ConversionResponse;
import com.danradchuk.exchangeratechallenge.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExchangeRateServiceTest {
    @Autowired
    private ExchangeRateService service;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private ExchangeRateApi api;

    @BeforeEach
    public void setUp() {
        cacheManager.getCache("exchange-rates").clear();
    }

    @Test
    public void shouldSucceedWhenBaseCurrencyIsUSD() {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD", Map.of("EUR", BigDecimal.ONE));
        when(api.fetchExchangeRates(Map.of("base", "USD"))).thenReturn(mockResp);

        ExchangeRateResponse resp = service.getExchangeRates("USD", null);
        assertEquals("USD", resp.getBase());
        assertNotNull(resp.getRates());
        assertEquals(BigDecimal.ONE, resp.getRates().get("EUR"));

        verify(api, times(1)).fetchExchangeRates(Map.of("base", "USD"));
    }

    @Test
    public void shouldSucceedWhenBaseCurrencyIsUSDAndTargetCurrencyIsEUR() {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD",
                Map.of("EUR", BigDecimal.ONE, "ASD", BigDecimal.TEN)
        );
        when(api.fetchExchangeRates(Map.of("base", "USD")))
                .thenReturn(mockResp);

        ExchangeRateResponse resp = service.getExchangeRates("USD", "EUR");
        assertEquals("USD", resp.getBase());
        assertNotNull(resp.getRates());
        assertEquals(1, resp.getRates().size());
        assertEquals(BigDecimal.ONE, resp.getRates().get("EUR"));


        verify(api, times(1)).fetchExchangeRates(Map.of("base", "USD"));
    }

    @Test
    public void shouldSucceedToConvertWhenBaseCurrencyIsUSDAndTargetCurrencyIsEUR() {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD",
                Map.of("EUR", BigDecimal.ONE, "ASD", BigDecimal.TEN)
        );
        when(api.fetchExchangeRates(Map.of("base", "USD"))).thenReturn(mockResp);

        ConversionResponse resp = service.convert("USD", "EUR", BigDecimal.valueOf(100));
        assertEquals("USD", resp.getFrom());
        assertNotNull(resp.getResult());
        assertEquals(1, resp.getResult().size());
        assertEquals(BigDecimal.valueOf(100), resp.getResult().get("EUR"));


        verify(api, times(1)).fetchExchangeRates(Map.of("base", "USD"));
    }

    @Test
    public void shouldSucceedToConvertWhenBaseCurrencyIsUSD() {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD",
                Map.of("EUR", BigDecimal.ONE, "ASD", BigDecimal.TEN)
        );
        when(api.fetchExchangeRates(Map.of("base", "USD"))).thenReturn(mockResp);

        ConversionResponse resp = service.convert("USD", null, BigDecimal.valueOf(100));
        assertEquals("USD", resp.getFrom());
        assertNotNull(resp.getResult());
        assertEquals(2, resp.getResult().size());
        assertEquals(BigDecimal.valueOf(100), resp.getResult().get("EUR"));
        assertEquals(BigDecimal.valueOf(1000), resp.getResult().get("ASD"));


        verify(api, times(1)).fetchExchangeRates(Map.of("base", "USD"));
    }

    @Test
    public void shouldSucceedWhenCacheIsNotEmptyAndBaseCurrencyIsUSD() {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD",
                Map.of("EUR", BigDecimal.ONE, "ASD", BigDecimal.TEN)
        );
        cacheManager.getCache("exchange-rates").put("USD", mockResp);
        when(api.fetchExchangeRates(Map.of("base", "USD")))
                .thenReturn(mockResp);

        ExchangeRateResponse resp = service.getExchangeRates("USD", "EUR");
        assertEquals("USD", resp.getBase());
        assertNotNull(resp.getRates());
        assertEquals(1, resp.getRates().size());
        assertEquals(BigDecimal.ONE, resp.getRates().get("EUR"));


        verify(api, times(0)).fetchExchangeRates(Map.of("base", "USD"));
    }
}
