package com.danradchuk.exchangeratechallenge;

import com.danradchuk.exchangeratechallenge.api.dto.ExchangeRateResponse;
import com.danradchuk.exchangeratechallenge.api.exception.ClientApiException;
import com.danradchuk.exchangeratechallenge.api.exception.ServerApiException;
import com.danradchuk.exchangeratechallenge.controller.ErrorResponse;
import com.danradchuk.exchangeratechallenge.controller.ExchangeRateController;
import com.danradchuk.exchangeratechallenge.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Map;

import static feign.Request.HttpMethod.GET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = ExchangeRateController.class)
public class ExchangeRateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ExchangeRateService service;

    @Test
    public void shouldSucceedIfToIsNull() throws Exception {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD", Map.of("EUR", BigDecimal.ONE));

        when(service.getExchangeRates("USD", null)).thenReturn(mockResp);

        MvcResult res = this.mockMvc.perform(
                        get("/api/v0/exchange-rates")
                                .contentType("application/json")
                                .accept("application/json")
                                .param("from", "USD")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respStr = res.getResponse().getContentAsString();
        assertNotNull(respStr);
        ExchangeRateResponse resp = objectMapper.readValue(respStr, ExchangeRateResponse.class);
        assertEquals(resp.getBase(), mockResp.getBase());
        assertEquals(resp.getRates(), mockResp.getRates());

        verify(service, times(1)).getExchangeRates("USD", null);
    }

    @Test
    public void shouldSucceedIfFromAndToAreNotNull() throws Exception {
        ExchangeRateResponse mockResp = new ExchangeRateResponse("USD", Map.of("EUR", BigDecimal.ONE));

        when(service.getExchangeRates("USD", "EUR")).thenReturn(mockResp);

        MvcResult res = this.mockMvc.perform(
                        get("/api/v0/exchange-rates")
                                .contentType("application/json")
                                .accept("application/json")
                                .param("from", "USD")
                                .param("to", "EUR")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respStr = res.getResponse().getContentAsString();
        assertNotNull(respStr);
        ExchangeRateResponse resp = objectMapper.readValue(respStr, ExchangeRateResponse.class);
        assertEquals(mockResp.getBase(), resp.getBase());
        assertEquals(mockResp.getRates(), resp.getRates());

        verify(service, times(1)).getExchangeRates("USD", "EUR");
    }

    @Test
    public void shouldReturn400ErrorWhenServiceThrowsClientApiException() throws Exception {
        Request req = Request.create(
                GET,
                "http://localhost/latest?from=USD&to=EUR",
                Map.of(),
                new byte[0],
                Charset.defaultCharset()
        );
        when(service.getExchangeRates("USD", "EUR")).thenThrow(new ClientApiException("Exception",
                Response.builder().request(req).build())
        );

        MvcResult res = this.mockMvc.perform(
                        get("/api/v0/exchange-rates")
                                .contentType("application/json")
                                .accept("application/json")
                                .param("from", "USD")
                                .param("to", "EUR")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respStr = res.getResponse().getContentAsString();
        assertNotNull(respStr);
        ErrorResponse resp = objectMapper.readValue(respStr, ErrorResponse.class);
        assertEquals(resp.getCode(), BAD_REQUEST.value());
        assertEquals(resp.getError(), "Bad Request");
        assertEquals(resp.getDetails(), "Exception");

        verify(service, times(1)).getExchangeRates("USD", "EUR");
    }

    @Test
    public void shouldReturn400ErrorWhenServiceThrowsServerApiException() throws Exception {
        Request req = Request.create(
                GET,
                "http://localhost/latest?from=USD&to=EUR",
                Map.of(),
                new byte[0],
                Charset.defaultCharset()
        );
        when(service.getExchangeRates("USD", "EUR")).thenThrow(new ServerApiException("Exception",
                Response.builder().request(req).build())
        );

        MvcResult res = this.mockMvc.perform(
                        get("/api/v0/exchange-rates")
                                .contentType("application/json")
                                .accept("application/json")
                                .param("from", "USD")
                                .param("to", "EUR")
                )
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respStr = res.getResponse().getContentAsString();
        assertNotNull(respStr);
        ErrorResponse resp = objectMapper.readValue(respStr, ErrorResponse.class);
        assertEquals(resp.getCode(), INTERNAL_SERVER_ERROR.value());
        assertEquals(resp.getError(), "Internal Server Error");
        assertEquals(resp.getDetails(), "Exception");

        verify(service, times(1)).getExchangeRates("USD", "EUR");
    }

    @Test
    public void shouldReturn400ErrorIfCircuitBreakerIsInOpenOrHalfOpenState() throws Exception {
        when(service.getExchangeRates("USD", "EUR")).thenThrow(
                CallNotPermittedException.createCallNotPermittedException(
                        CircuitBreaker.ofDefaults("exchange-rate")
                )
        );

        MvcResult res = this.mockMvc.perform(
                        get("/api/v0/exchange-rates")
                                .contentType("application/json")
                                .accept("application/json")
                                .param("from", "USD")
                                .param("to", "EUR")
                )
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respStr = res.getResponse().getContentAsString();
        assertNotNull(respStr);
        ErrorResponse resp = objectMapper.readValue(respStr, ErrorResponse.class);
        assertEquals(resp.getCode(), SERVICE_UNAVAILABLE.value());
        assertEquals(resp.getError(), "Service temporarily unavailable");
        assertEquals(resp.getDetails(), "CircuitBreaker 'exchange-rate' is CLOSED and does not permit further calls");

        verify(service, times(1)).getExchangeRates("USD", "EUR");
    }
}
