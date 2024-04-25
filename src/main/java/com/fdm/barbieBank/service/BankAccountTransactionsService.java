package com.fdm.barbieBank.service;

import com.fdm.barbieBank.model.BankAccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import com.fdm.barbieBank.model.ChartPoint;
import com.fdm.barbieBank.model.MonthlyDebitAndCredit;
import com.fdm.barbieBank.model.BankAccountTransactions;
import com.fdm.barbieBank.repository.BankAccountTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

import com.fdm.barbieBank.utils.DateUtils;

@Service
public class BankAccountTransactionsService {
	
	@Autowired
    private BankAccountTransactionsRepository transactionsRepository;

    public BankAccountTransactions saveTransaction(BankAccountTransactions transaction) {
        return transactionsRepository.save(transaction);
    }

    public List<BankAccountTransactions> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    public BankAccountTransactions getTransactionByTransactionId(Long id) {
        return transactionsRepository.findById(id).orElse(null);
    }

    public List<BankAccountTransactions> getTransactionsByBankAccount(BankAccount bankAccount) {
        return transactionsRepository.findByBankAccount(bankAccount);
    }

    public void deleteTransaction(Long id) {
        transactionsRepository.deleteById(id);
    }

    public void deleteAllTransactions() {
        transactionsRepository.deleteAll();
    }

    public long countTransactions() {
        return transactionsRepository.count();
    }
    
    public List<ChartPoint> getBankAccountChart(BankAccount bankAccount, Date startDate, Date endDate){
    
    	//to retrieve debit and credit of bank account at end of month
    	List<MonthlyDebitAndCredit> monthlyDebitAndCredit = transactionsRepository.
    			findByBankAccountSumDebitAndCreditByMonthAndYearClass(bankAccount,startDate, endDate);
    
    	//to save chart points which can be used to display charts
    	List<ChartPoint> bankAccountChart = new ArrayList<ChartPoint>();
    
    	//get the latest current balance of the bank account(this month's balance)
    	double currentBalance = bankAccount.getBalance();
    
    	for(MonthlyDebitAndCredit perMonth: monthlyDebitAndCredit) {
    		//for x values - monthAndYear (the balance at end of that month and year)
    		//for y values - balance (the remaining balance for that month)
    		
    		//for month and year
    		int month = perMonth.getMonth();
    		int year = perMonth.getYear();
    		String monthAndYear = DateUtils.getMonthInString(month) + "-" + String.valueOf(year);
    		ChartPoint bankAccountChartPoint =  new ChartPoint(monthAndYear, currentBalance);
    		bankAccountChart.add(bankAccountChartPoint);
    		
    		//for balance - since the query fetches result in descending order (latest to earliest)
    		//we will get balance for the month before instead next month per iteration for permonth
   
    		double debit = perMonth.getDebit().doubleValue();
    		double credit = perMonth.getCredit().doubleValue();
    	
    		//add credit to balance for last month meaning the money spent this month (it was there last month hence add)
    		currentBalance += credit;
    		//minus debit from balance for last month meaning the money deposit this month (it was  not there last month hence minus)
    		currentBalance -= debit;
    		
    	}
    	return bankAccountChart;
    }
    // Add additional methods as needed for business logic

}
