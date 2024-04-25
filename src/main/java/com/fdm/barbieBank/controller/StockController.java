package com.fdm.barbieBank.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Collections; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fdm.barbieBank.service.StockService;
import com.fdm.barbieBank.utils.SearchResponse;
import com.fdm.barbieBank.utils.Stock;

@Controller
public class StockController {

    private final StockService stockService;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public String goToTradingPage() {
        return "stocks"; 
    }

    @GetMapping("/stock")
    @ResponseBody
    public Stock getStock(@RequestParam String symbol) {
        return stockService.getStock(symbol);
    }

    @GetMapping("/search")
    @ResponseBody
    public Map<String, Object> searchStock(@RequestParam String keywords) {
        SearchResponse response = stockService.searchStock(keywords);

        List<Map<String, String>> results;

        if (response.getBestMatches() != null) {
            results = response.getBestMatches().stream()
                    .filter(match -> match.getSymbol() != null && match.getName() != null)
                    .map(match -> Map.of("symbol", match.getSymbol(), "name", match.getName()))
                    .collect(Collectors.toList());
        } else {
            results = Collections.emptyList();
        }

        return Map.of("bestMatches", results);
    }
}

