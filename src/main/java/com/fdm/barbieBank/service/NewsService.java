package com.fdm.barbieBank.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsService {

    @Value("${newsapi.key}")
    private String apiKey;

    public String getTopHeadlines() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=" + apiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String searchArticles(SearchParameters parameters) {
        RestTemplate restTemplate = new RestTemplate();
        String url = buildSearchUrl(parameters);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private String buildSearchUrl(SearchParameters parameters) {
        StringBuilder url = new StringBuilder("https://newsapi.org/v2/everything?apiKey=" + apiKey);
        
        // Append all parameters to the URL
        if (parameters.q != null) url.append("&q=").append(parameters.q);
        if (parameters.searchIn != null) url.append("&searchIn=").append(parameters.searchIn);
        if (parameters.sources != null) url.append("&sources=").append(parameters.sources);
        if (parameters.domains != null) url.append("&domains=").append(parameters.domains);
        if (parameters.excludeDomains != null) url.append("&excludeDomains=").append(parameters.excludeDomains);
        if (parameters.from != null) url.append("&from=").append(parameters.from);
        if (parameters.to != null) url.append("&to=").append(parameters.to);
        if (parameters.language != null) url.append("&language=").append(parameters.language);
        if (parameters.sortBy != null) url.append("&sortBy=").append(parameters.sortBy);
        url.append("&pageSize=").append(parameters.pageSize);
        url.append("&page=").append(parameters.page);

        return url.toString();
    }

    public static class SearchParameters {
        public String q, searchIn, sources, domains, excludeDomains, from, to, language, sortBy;
        public int pageSize, page;

        public SearchParameters(String q, String searchIn, String sources, String domains, String excludeDomains, String from, String to, String language, String sortBy, int pageSize, int page) {
            this.q = q;
            this.searchIn = searchIn;
            this.sources = sources;
            this.domains = domains;
            this.excludeDomains = excludeDomains;
            this.from = from;
            this.to = to;
            this.language = language;
            this.sortBy = sortBy;
            this.pageSize = pageSize;
            this.page = page;
        }
    }
}


