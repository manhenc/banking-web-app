package com.fdm.barbieBank.model;

import java.math.BigDecimal;

public class MonthlyDebitAndCredit {

	private BigDecimal debit;
	private BigDecimal credit;
	private int month;
	private int year;
	
	public MonthlyDebitAndCredit(BigDecimal debit, BigDecimal credit, int month, int year) {
		super();
		this.debit = debit;
		this.credit = credit;
		this.month = month;
		this.year = year;
	}
	
	public BigDecimal getDebit() {
		return debit;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	
	

}
