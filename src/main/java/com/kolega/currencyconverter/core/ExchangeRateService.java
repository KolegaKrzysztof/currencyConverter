package com.kolega.currencyconverter.core;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ExchangeRateService {
    ExchangeRateGetter exchangeRateGetter = ExchangeRateGetter.getInstance();
    public Double getExchangeRate() throws URISyntaxException, IOException, InterruptedException {
        return exchangeRateGetter.getExchangeRate();
    }
}
