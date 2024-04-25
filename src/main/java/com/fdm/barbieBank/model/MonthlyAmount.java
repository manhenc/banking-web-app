package com.fdm.barbieBank.model;

import java.math.BigDecimal;

public class MonthlyAmount {

	private BigDecimal amount;
	private int month;
	private int year;
	
	public MonthlyAmount(BigDecimal amount, int month, int year) {
		super();
		this.amount = amount;
		this.month = month;
		this.year = year;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	
	

}
