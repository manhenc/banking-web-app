package com.fdm.barbieBank.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdm.barbieBank.service.ForexDatabaseService;
import com.fdm.barbieBank.service.ForexDatabaseWebClient;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.barbieBank.model.Currency;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ForexDatabaseServiceTest {
	
	@Mock
	ForexDatabaseWebClient webClient;
	
	@InjectMocks
	ForexDatabaseService forexDbService;
	
	@Test
	public void test_findForexRates_returns_updatedMapOfCurrencyCodeAndRates() {
		Double baseUsdRate = 1.0;
		Map<String, Double> mappedRates;
		
		try {
			mappedRates = forexDbService.findForexRates();
			
			verify(webClient, times(1)).updateForexRates();
			assertEquals(mappedRates.get("USD"),baseUsdRate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test_findCurrencyNames_returns_mapOfCurrencyCodeAndNames() {
		String usdName = "United States Dollar";
		Map<String, String> mappedNames;
		
		try {
			mappedNames = forexDbService.findCurrencyNames();
			
			assertEquals(mappedNames.get("USD"),usdName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_searchByCodeOrName_returns_listOfMatchingCurrencies_whenSearchByCode() {
		try {
			List<Currency> searchResults = forexDbService.searchByCodeOrName("aud");
			assertEquals(searchResults.get(0).getCode(),"AUD");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_searchByCodeOrName_returns_listOfMatchingCurrencies_whenSearchByName() {
		try {
			List<Currency> searchResults = forexDbService.searchByCodeOrName("australian");
			assertEquals(searchResults.get(0).getCode(),"AUD");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_calculateCurrencyConversion_returns_stringOfConvertedRoundedAmount() throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String userDir = System.getProperty("user.dir");
		
		File forexRatesFile = new File(userDir+"//src//main//resources//forexData.json");
		Map<String,Double> rates = mapper.readValue(forexRatesFile, new TypeReference<Map<String, Double>>(){});
		
		Double sgdRate = rates.get("SGD");
		Double roundedSgdRate = (double) Math.round(sgdRate*100);
		roundedSgdRate = roundedSgdRate/100;
		DecimalFormat format = new DecimalFormat("0.#");
		String roundedSgdString = format.format(roundedSgdRate);
		
		
		String convertedOneUsdToSgd = forexDbService.calculateCurrencyConversion("USD", "SGD", "1");
		
		assertEquals(convertedOneUsdToSgd,roundedSgdString);
	}
}
