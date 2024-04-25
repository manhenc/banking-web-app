package com.fdm.barbieBank.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;

@Component
@Entity
public class BankAccountTransactions {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bank_transaction_id;

	private Date transaction_date;
	private String description;
	private BigDecimal credit;
	private BigDecimal debit;

	@ManyToOne
	@JoinColumn(name = "bankAccountId")
	private BankAccount bankAccount;

	public BankAccountTransactions() {
		super();
	}

	public BankAccountTransactions(Date transaction_date, String description, BigDecimal credit, BigDecimal debit,
			BankAccount bankAccount) {
		super();
		this.transaction_date = transaction_date;
		this.description = description;
		this.credit = credit; //minus
		this.debit = debit; //plus
		this.bankAccount = bankAccount;
	}

	public Long getBank_transaction_id() {
		return bank_transaction_id;
	}

	public void setBank_transaction_id(Long bank_transaction_id) {
		this.bank_transaction_id = bank_transaction_id;
	}

	public Date getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

}
