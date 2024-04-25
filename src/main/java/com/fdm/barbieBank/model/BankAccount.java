package com.fdm.barbieBank.model;

import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Component
@Entity
public class BankAccount {

	@Id
	@SequenceGenerator(name = "BANK_ACCOUNT_SEQ_GNTR", sequenceName = "BANK_ACCOUNT_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANK_ACCOUNT_SEQ_GNTR")
	@Column(unique = true)
	private long bankAccountId;

	@Column(unique = true, length=12)
	private long accountNumber;
	private double balance;

	private Date dateCreated;
	private Date dateClosed;

	@ManyToOne
	@JoinColumn(name = "account_holder_user")
	private User user;

	public BankAccount() {
		super();
	}

	public BankAccount(long accountNumber, double balance, Date dateCreated, Date dateClosed) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.dateCreated = dateCreated;
		this.dateClosed = dateClosed;
	}

	public long getBankAccountId() {
		return bankAccountId;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public User getUser() {
		return user;
	}

	public void setBankAccountId(long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "BankAccount [bankAccountId=" + bankAccountId + ", accountNumber=" + accountNumber + ", balance="
				+ balance + ", dateCreated=" + dateCreated + ", dateClosed=" + dateClosed + ", user=" + user + "]";
	}

}
