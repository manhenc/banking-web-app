package com.fdm.barbieBank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import com.fdm.barbieBank.utils.SearchResponse;
import com.fdm.barbieBank.utils.Stock;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Value("${alphavantage.api.key}")
    private String apiKey;

    public Stock getStock(String symbol) {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey;
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .filter(logResponse())
                .build();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Stock.class)
                .block();
    }

    public SearchResponse searchStock(String keywords) {
        String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + keywords + "&apikey=" + apiKey;
        logger.info("Requesting search with URL: {}", url);
        WebClient webClient = WebClient.builder()
                .filter(logResponse())
                .build();

        SearchResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .block();

        logger.info("Received search response: {}", response);
        return response;
    }

    private ExchangeFilterFunction logResponse() {
        return (clientRequest, next) -> {
            next.exchange(clientRequest)
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .doOnNext(body -> logger.info("Raw JSON response: {}", body))
                .subscribe();
            return next.exchange(clientRequest);
        };
    }
}


