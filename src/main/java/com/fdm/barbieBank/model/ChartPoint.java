package com.fdm.barbieBank.model;

public class ChartPoint {

	private String monthAndYear;
	private double amount;
	
	public ChartPoint(String monthAndYear, double amount) {
		super();
		this.monthAndYear = monthAndYear;
		this.amount = amount;
	}
	public String getMonthAndYear() {
		return monthAndYear;
	}
	public double getAmount() {
		return amount;
	}
	public void setMonthAndYear(String monthAndYear) {
		this.monthAndYear = monthAndYear;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
