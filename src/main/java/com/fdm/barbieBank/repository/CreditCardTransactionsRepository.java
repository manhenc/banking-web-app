package com.fdm.barbieBank.repository;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardTransactions;
import com.fdm.barbieBank.model.MonthlyAmount;
import com.fdm.barbieBank.model.MonthlyDebitAndCredit;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardTransactionsRepository extends JpaRepository<CreditCardTransactions, Long> {

    List<CreditCardTransactions> findByCreditCard(CreditCard creditCard);

    List<CreditCardTransactions> findByCreditCardAndTransactionDateBetween(
            CreditCard creditCard, Date startDate, Date endDate);
    // method name need to match attribute name

    // Additional custom queries can be added here
    
	//for credit card charts
	//displays the total amount spent at end of month of a credit card over a time range
    @Query("SELECT new com.fdm.barbieBank.model.MonthlyAmount(SUM(t.amount),"
			+ "MONTH(t.transactionDate),YEAR(t.transactionDate)) "
			  + "FROM CreditCardTransactions AS t "
			  + "WHERE t.creditCard= :creditCard "
			  + "AND t.amount > 0 "
			  + "AND DATE(t.transactionDate) >= :startDate "
			  + "AND DATE(t.transactionDate) <= :endDate "
			  + "GROUP BY MONTH(t.transactionDate),YEAR(t.transactionDate) "
			  + "ORDER BY YEAR(t.transactionDate), MONTH(t.transactionDate)"
			  )
	List<MonthlyAmount> findByCreditCardSumAmountByMonthAndYearClass(CreditCard creditCard, Date startDate, Date endDate);
}