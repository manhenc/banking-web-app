package com.fdm.barbieBank.utils;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stock {
    @JsonProperty("1. Information")
    private String information;
    @JsonProperty("2. Symbol")
    private String symbol;
    @JsonProperty("3. Last Refreshed")
    private String lastRefreshed;
    @JsonProperty("4. Output Size")
    private String outputSize;
    @JsonProperty("5. Time Zone")
    private String timeZone;

    @JsonProperty("Time Series (Daily)")
    private Map<String, StockData> timeSeries;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public String getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Map<String, StockData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Map<String, StockData> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public static class StockData {
        @JsonProperty("1. open")
        private String open;
        @JsonProperty("2. high")
        private String high;
        @JsonProperty("3. low")
        private String low;
        @JsonProperty("4. close")
        private String close;
        @JsonProperty("5. volume")
        private String volume;

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }
    }
}




