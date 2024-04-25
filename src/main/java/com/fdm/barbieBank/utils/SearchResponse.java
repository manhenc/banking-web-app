package com.fdm.barbieBank.utils;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResponse {
    private List<Match> bestMatches;

    public List<Match> getBestMatches() {
        return bestMatches;
    }

    public void setBestMatches(List<Match> bestMatches) {
        this.bestMatches = bestMatches;
    }

    public static class Match {
        @JsonProperty("1. symbol")
        private String symbol;
        @JsonProperty("2. name")
        private String name;
        @JsonProperty("3. type")
        private String type;
        @JsonProperty("4. region")
        private String region;
        @JsonProperty("5. marketOpen")
        private String marketOpen;
        @JsonProperty("6. marketClose")
        private String marketClose;
        @JsonProperty("7. timezone")
        private String timezone;
        @JsonProperty("8. currency")
        private String currency;
        @JsonProperty("9. matchScore")
        private String matchScore;

        public String getMatchScore() {
            return matchScore;
        }
        public void setMatchScore(String matchScore) {
            this.matchScore = matchScore;
        }
        public String getCurrency() {
            return currency;
        }
        public void setCurrency(String currency) {
            this.currency = currency;
        }
        public String getTimezone() {
            return timezone;
        }
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
        public String getMarketClose() {
            return marketClose;
        }
        public void setMarketClose(String marketClose) {
            this.marketClose = marketClose;
        }
        public String getMarketOpen() {
            return marketOpen;
        }
        public void setMarketOpen(String marketOpen) {
            this.marketOpen = marketOpen;
        }
        public String getRegion() {
            return region;
        }
        public void setRegion(String region) {
            this.region = region;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getSymbol() {
            return symbol;
        }
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
