package com.fdm.barbieBank.model;

import java.util.Map;

public class ForexRates {
	private boolean valid;
	private int updated;
	private String base;
	private Map<String,Double> rates;
	
	public ForexRates() {
		super();
	}
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public Map<String, Double> getRates() {
		return rates;
	}
	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
	
	
}
