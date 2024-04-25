package com.fdm.barbieBank.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;

@Component
@Entity
public class CreditCardTransactions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long credit_transaction_id;

	private Date transactionDate;
	private String description;
	private String type;
	private BigDecimal amount;
	private Byte paid;

	@ManyToOne
	@JoinColumn(name = "cardId")
	private CreditCard creditCard;

	public CreditCardTransactions() {
		super();
	}

	public CreditCardTransactions(Date transaction_date, String description, String type, BigDecimal amount, byte paid,
			CreditCard creditCard) {
		super();
		this.transactionDate = transaction_date;
		this.description = description;
		this.type = type;
		this.amount = amount;
		this.paid = paid;
		this.creditCard = creditCard;
	}

	public long getCredit_transaction_id() {
		return credit_transaction_id;
	}

	public void setCredit_transaction_id(Long credit_transaction_id) {
		this.credit_transaction_id = credit_transaction_id;
	}

	public Date getTransaction_date() {
		return transactionDate;
	}

	public void setTransaction_date(Date datetime) {
		this.transactionDate = datetime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Byte getPaid() {
		return paid;
	}

	public void setPaid(Byte paid) {
		this.paid = paid;
	}

}
