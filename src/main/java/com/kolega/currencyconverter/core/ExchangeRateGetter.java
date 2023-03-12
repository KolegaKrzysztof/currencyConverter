package com.kolega.currencyconverter.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class ExchangeRateGetter {

    private final static ExchangeRateGetter instance = new ExchangeRateGetter();

    private ExchangeRateGetter(){
    }

    public double getExchangeRate() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> getResponse = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder(
                        new URI("https://api.nbp.pl/api/exchangerates/rates/a/gbp/?format=json")
                        ).build(),
                HttpResponse.BodyHandlers.ofString());
        JsonElement element = new JsonParser().parse(getResponse.body());

        return element.getAsJsonObject().getAsJsonArray("rates")
                .get(0).getAsJsonObject().get("mid").getAsDouble();
    }

    public static ExchangeRateGetter getInstance() {
        return instance;
    }
}
