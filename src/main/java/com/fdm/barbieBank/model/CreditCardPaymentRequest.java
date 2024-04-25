package com.fdm.barbieBank.model;

import java.math.BigDecimal;

public class CreditCardPaymentRequest {
	private String cardNumber;
	private int expirydatemonth;
	private int expirydateyear;
	private String cvv;
	private String fromCurrency;
	private BigDecimal amount;
	private String merchant;
	private String mcc;


	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getExpirydatemonth() {
		return expirydatemonth;
	}
	public void setExpirydatemonth(int expirydatemonth) {
		this.expirydatemonth = expirydatemonth;
	}
	public int getExpirydateyear() {
		return expirydateyear;
	}
	public void setExpirydateyear(int expirydateyear) {
		this.expirydateyear = expirydateyear;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getFromCurrency() {
		return fromCurrency;
	}
	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}


}
