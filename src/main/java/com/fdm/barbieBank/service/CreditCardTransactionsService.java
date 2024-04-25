package com.fdm.barbieBank.service;

import com.fdm.barbieBank.model.ChartPoint;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardTransactions;
import com.fdm.barbieBank.model.MonthlyAmount;
import com.fdm.barbieBank.repository.CreditCardTransactionsRepository;
import com.fdm.barbieBank.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CreditCardTransactionsService {

	@Autowired
	private CreditCardTransactionsRepository transactionsRepository;

	// Business logic and operations related to credit card transactions go here

	public CreditCardTransactions saveTransaction(CreditCardTransactions transaction) {
		// Additional processing, validation, or calculations can be done here before
		// saving
		return transactionsRepository.save(transaction);
	}

	public List<CreditCardTransactions> getAllTransactions() {
		return transactionsRepository.findAll();
	}

	public CreditCardTransactions getTransactionById(Long id) {
		return transactionsRepository.findById(id).orElse(null);
	}

	public List<CreditCardTransactions> getTransactionsByCreditCard(CreditCard creditCard) {
		return transactionsRepository.findByCreditCard(creditCard);
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

	public String getTransactionTypeByMCC(int mcc) {
		if (mcc >= 1 && mcc <= 1499) {
			return "Agricultural Services";
		} else if (mcc >= 1500 && mcc <= 2999) {
			return "Contracted Services";
		} else if (mcc >= 3000 && mcc <= 3299) {
			return "Airlines";
		} else if (mcc >= 3300 && mcc <= 3499) {
			return "Car Rental";
		} else if (mcc >= 3500 && mcc <= 3999) {
			return "Lodging";
		} else if (mcc >= 4000 && mcc <= 4799) {
			return "Transportation Services";
		} else if (mcc >= 4800 && mcc <= 4999) {
			return "Utility Services";
		} else if (mcc >= 5000 && mcc <= 5599) {
			return "Retail Outlet Services";
		} else if (mcc >= 5600 && mcc <= 5699) {
			return "Clothing Stores";
		} else if (mcc >= 5700 && mcc <= 7299) {
			return "Miscellaneous Stores";
		} else if (mcc >= 7300 && mcc <= 7999) {
			return "Business Services";
		} else if (mcc >= 8000 && mcc <= 8999) {
			return "Professional Services and Membership Organizations";
		} else if (mcc >= 9000 && mcc <= 9999) {
			return "Government Services";
		} else {
			return "-";
		}
	}

    // public List<CreditCardTransactions> findTransactionsByMerchantName(String
    // merchantName) {
    // return
    // transactionsRepository.findByMerchant_nameContainingIgnoreCase(merchantName);
    // }
    
    public List<ChartPoint> getCreditCardChart(CreditCard creditCard, Date startDate, Date endDate){
        
    	//to retrieve debit and credit of bank account at end of month
    	List<MonthlyAmount> monthlyAmount = transactionsRepository.findByCreditCardSumAmountByMonthAndYearClass(creditCard, startDate, endDate);
    
    	//to save chart points which can be used to display charts
    	List<ChartPoint> creditCardChart = new ArrayList<ChartPoint>();
    
    
    	for(MonthlyAmount perMonth: monthlyAmount) {
    		//for x values - monthAndYear (the balance at end of that month and year)
    		//for y values - balance (the remaining balance for that month)
    		
    		//for month and year
    		double amount = perMonth.getAmount().doubleValue();
    		int month = perMonth.getMonth();
    		int year = perMonth.getYear();
    		String monthAndYear = DateUtils.getMonthInString(month) + "-" + String.valueOf(year);
    		ChartPoint creditCardChartPoint =  new ChartPoint(monthAndYear, amount);
    		creditCardChart.add(creditCardChartPoint);
    		
    	}
    	return creditCardChart;
    }


}