package com.fdm.barbieBank.model;

import java.util.Map;

public class ForexCurrencyName {
	private boolean valid;
	private Map<String,String> currencies;
	
	public ForexCurrencyName() {
		super();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Map<String, String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, String> currencies) {
		this.currencies = currencies;
	}
	
	
}
