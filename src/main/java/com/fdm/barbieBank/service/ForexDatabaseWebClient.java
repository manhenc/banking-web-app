package com.fdm.barbieBank.service;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.barbieBank.model.ForexRates;
import com.fdm.barbieBank.model.ForexCurrencyName;

@Service
public class ForexDatabaseWebClient {
	private final WebClient webClient;
	
	public ForexDatabaseWebClient() {
		super();
		this.webClient = WebClient.builder().baseUrl("https://currencyapi.net")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Assuming the API expects an authorization header named "key"
				.build();
	}
	
	public void updateForexRates(){
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		Writer fileWriter;
		
		Map<String,Double> forexRates = webClient.get().uri(builder -> builder.path("api/v1/rates") .queryParam("key", "YHa9LsuzHejN6g7n66KPJFs8oBCmZfpbBpM8").build())
				.retrieve()
				.bodyToMono(ForexRates.class).block().getRates();
		
		try {
			fileWriter = new FileWriter(new File(userDir+"//src//main//resources//forexData.json"),false);
			mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, forexRates);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getCurrencyName() {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		Writer fileWriter;
		
		Map<String,String> currencyNames = webClient.get().uri(builder -> builder.path("api/v1/currencies") .queryParam("key", "YHa9LsuzHejN6g7n66KPJFs8oBCmZfpbBpM8").build())
				.retrieve()
				.bodyToMono(ForexCurrencyName.class).block().getCurrencies();
		
		try {
			fileWriter = new FileWriter(new File(userDir+"//src//main//resources//currencyNames.json"),false);
			mapper.writerWithDefaultPrettyPrinter().writeValue(fileWriter, currencyNames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
