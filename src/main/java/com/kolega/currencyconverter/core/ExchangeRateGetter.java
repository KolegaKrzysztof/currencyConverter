package com.kolega.currencyconverter.core;

import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ExchangeRateGetter {

    private final static ExchangeRateGetter instance = new ExchangeRateGetter();

    private ExchangeRateGetter(){
    }

    public double getExchangeRate(String bidOrAsk) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> getResponse = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder(
                        new URI("https://api.nbp.pl/api/exchangerates/rates/c/gbp/today/?format=json")
                        ).build(), HttpResponse.BodyHandlers.ofString());

        return JsonParser.parseString(getResponse.body()).getAsJsonObject().getAsJsonArray("rates")
                .get(0).getAsJsonObject().get(bidOrAsk).getAsDouble();

    }

    public static ExchangeRateGetter getInstance() {
        return instance;
    }

}
