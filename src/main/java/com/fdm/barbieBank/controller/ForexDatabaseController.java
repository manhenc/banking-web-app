package com.fdm.barbieBank.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fdm.barbieBank.service.ForexDatabaseService;
import com.fdm.barbieBank.model.Currency;
import com.fdm.barbieBank.utils.ForexUtils;

@Controller
public class ForexDatabaseController {

	@Autowired
	private ForexDatabaseService forexDbService;

	@GetMapping("/forexList")
	public String getForexRates(Model model) throws StreamReadException, DatabindException, IOException {
		Map<String, Double> rates = forexDbService.findForexRates();
		Map<String, String> currencyNames = forexDbService.findCurrencyNames();

		List<Currency> currencies = new ArrayList<Currency>();
		ForexUtils forexUtil = new ForexUtils();

		// maps all currency details and adds the currency into list of currencies
		for (Entry<String, Double> entry : rates.entrySet()) {
			String currencyCode = entry.getKey();
			Double roundedRate = forexUtil.roundToFiveSF(entry.getValue());
			Currency oneCurrency = forexDbService.mapToCurrency(currencyCode, currencyNames.get(currencyCode), roundedRate);
			currencies.add(oneCurrency);
		}

		model.addAttribute("currencies", currencies);
		return "forexList";
	}

	@GetMapping("/forexList/search")
	public String searchForexRates(@RequestParam(required = false) String search, Model model)
			throws StreamReadException, DatabindException, IOException {
		List<Currency> searchResults = forexDbService.searchByCodeOrName(search);
		model.addAttribute("currencies", searchResults);
		return "forexList";
	}
	
	@GetMapping("/forexConvertor")
	public String goToCurrencyCalculator(Model model) throws StreamReadException, DatabindException, IOException {
		List<Currency> storedCurrencies = forexDbService.findListOfStoredUnroundedCurrencies();
		model.addAttribute("currencies", storedCurrencies);
		return "forexConvertor";
	}
	
	@GetMapping("/forexConvertor/convert")
	public String convertCurrency(Model model, @RequestParam(required=true) String fromCurrency, @RequestParam(required=true) String toCurrency, @RequestParam(required=true) String amount) throws StreamReadException, DatabindException, IOException {
		String convertedAmt = forexDbService.calculateCurrencyConversion(fromCurrency, toCurrency, amount);
		model.addAttribute("result", convertedAmt);
		model.addAttribute("toCurrencyCode", toCurrency);
		
		// pass in forex data to refreshed forexConverter page
		List<Currency> storedCurrencies = forexDbService.findListOfStoredUnroundedCurrencies();
		model.addAttribute("currencies", storedCurrencies);
		return "forexConvertor";
	}

}
