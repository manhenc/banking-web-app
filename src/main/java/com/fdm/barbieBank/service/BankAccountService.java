package com.fdm.barbieBank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.BankAccountRepository;

@Service
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	public BankAccount createBankAccount(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	public List<BankAccount> displayListOfBankAccountsOfUser(User user) {
		return bankAccountRepository.findByUser(user);
	}
	
	public double getTotalBalanceOfAllAccounts(User user) {
		long userId = user.getUserID();
		return bankAccountRepository.findSumOfAllBankAccountBalanceOfUser(userId);
	}
	
	public Optional<BankAccount> getBankAccountDetails(long bankAccountId){
		return bankAccountRepository.findById(bankAccountId);
	}
	
	public Optional<BankAccount> getBankAccountDetailsByAccountNumber(long accountNumber){
		return bankAccountRepository.findByAccountNumber(accountNumber);
	}
	
	public BankAccount depositBankAccount(BankAccount bankAccount, double depositAmount) {
		double newBalance = bankAccount.getBalance() + depositAmount;
		bankAccount.setBalance(newBalance);
		return bankAccountRepository.save(bankAccount);
		
	}
	
	public BankAccount withdrawBankAccount(BankAccount bankAccount, double withdrawAmount) {
		double newBalance = bankAccount.getBalance() - withdrawAmount;
		bankAccount.setBalance(newBalance);
		return bankAccountRepository.save(bankAccount);
	}
	
	public void transferFromAndToBankAccounts(BankAccount sourceBankAccount, BankAccount destinationBankAccount, double transferAmount) {
		withdrawBankAccount(sourceBankAccount, transferAmount);
		depositBankAccount(destinationBankAccount, transferAmount);
	}
	
	public BankAccount closeAccount(BankAccount closingBankAccount, Date closingDate) {
		closingBankAccount.setDateClosed(closingDate);
		return bankAccountRepository.save(closingBankAccount);
	}
	
	public BankAccount closeAccount(BankAccount closingBankAccount, BankAccount transfereeBankAccount, Date closingDate) {
		double amount = closingBankAccount.getBalance();
		transferFromAndToBankAccounts(closingBankAccount, transfereeBankAccount, amount);
		return closeAccount(closingBankAccount, closingDate);
	}


	public List<BankAccount> getAllBankAccounts() {
		return bankAccountRepository.findAll();
	}
	
	public List<BankAccount> getOpenBankAccountsOfUser(User user){
		return bankAccountRepository.findByUserAndDateClosedIsNull(user);
	}
	
	public List<BankAccount> getClosedBankAccountsOfUser(User user){
		return bankAccountRepository.findByUserAndDateClosedIsNotNull(user);
	}

}
