package com.fdm.barbieBank.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.barbieBank.model.Currency;
import com.fdm.barbieBank.utils.ForexUtils;

@Service
public class ForexDatabaseService {

	@Autowired
	private ForexDatabaseWebClient webClient;

	public Map<String, Double> findForexRates() throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");

		webClient.updateForexRates();
		File forexRatesFile = new File(userDir + "//src//main//resources//forexData.json");

		Map<String, Double> mappedRates = mapper.readValue(forexRatesFile, new TypeReference<Map<String, Double>>() {
		});

		return mappedRates;
	}

	public Map<String, String> findCurrencyNames() throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		File currencyNamesFile = new File(userDir + "//src//main//resources//currencyNames.json");

		// only write currency names data into currencyNames.json if it is empty
		if (currencyNamesFile.length() == 0)
			webClient.getCurrencyName();

		Map<String, String> mappedCurrencyNames = mapper.readValue(currencyNamesFile,
				new TypeReference<Map<String, String>>() {
				});

		return mappedCurrencyNames;
	}
	
	public Currency mapToCurrency(String code,String name,Double rate) {
		Currency currency = new Currency();
		currency.setCode(code);
		currency.setName(name);
		currency.setRate(rate);
		return currency;
	}
	
	public List<Currency> searchByCodeOrName(String searchTerm) throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		ForexUtils util = new ForexUtils();
		
		File currencyNamesFile = new File(userDir+"//src//main//resources//currencyNames.json");
		Map<String,String> currencyNames = mapper.readValue(currencyNamesFile, new TypeReference<Map<String, String>>(){});
		File forexRatesFile = new File(userDir+"//src//main//resources//forexData.json");
		Map<String,Double> rates = mapper.readValue(forexRatesFile, new TypeReference<Map<String, Double>>(){});
		
		List<Currency> searchResults = new ArrayList<Currency>();
		// iterate through currencyNames and add currency that matches searchTerm to searchResults
		for (Entry<String, String> entry : currencyNames.entrySet()) {
			if(entry.getKey().contains(searchTerm.toUpperCase()) || entry.getValue().toLowerCase().contains(searchTerm.toLowerCase())) {
				Currency currency = mapToCurrency(entry.getKey(), entry.getValue(), util.roundToFiveSF(rates.get(entry.getKey())));
				searchResults.add(currency);
			}
		}
		
		return searchResults;
	}
	
	public List<Currency> findListOfStoredUnroundedCurrencies() throws StreamReadException, DatabindException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		
		File currencyNamesFile = new File(userDir+"//src//main//resources//currencyNames.json");
		Map<String,String> currencyNames = mapper.readValue(currencyNamesFile, new TypeReference<Map<String, String>>(){});
		File forexRatesFile = new File(userDir+"//src//main//resources//forexData.json");
		Map<String,Double> rates = mapper.readValue(forexRatesFile, new TypeReference<Map<String, Double>>(){});
		
		List<Currency> currencies = new ArrayList<Currency>();
		for (Entry<String,String> entry : currencyNames.entrySet()) {
			Currency currency = mapToCurrency(entry.getKey(),entry.getValue(),rates.get(entry.getKey()));
			currencies.add(currency);
		}
		
		return currencies;
	}
	
	public String calculateCurrencyConversion(String fromCode, String toCode, String amt) throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		
		File forexRatesFile = new File(userDir+"//src//main//resources//forexData.json");
		Map<String,Double> rates = mapper.readValue(forexRatesFile, new TypeReference<Map<String, Double>>(){});
		
		Double amountToConvert = Double.parseDouble(amt);
		Double calculatedAmt = (amountToConvert/rates.get(fromCode)) * rates.get(toCode);
		
		// round to 2dp
		Double roundedAmt = (double) Math.round(calculatedAmt * 100);
		roundedAmt = roundedAmt/100;
		
		DecimalFormat format = new DecimalFormat("0.#");
		return format.format(roundedAmt);
	}
}
 