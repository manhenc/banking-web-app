package com.fdm.barbieBank.repository;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.MonthlyDebitAndCredit;
import com.fdm.barbieBank.model.BankAccountTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankAccountTransactionsRepository extends JpaRepository<BankAccountTransactions, Long> {

	List<BankAccountTransactions> findByBankAccount(BankAccount bankAccount);
	
	//for bank account charts
	//displays the total debit and credit at end of month of a bank account over a time range
	@Query("SELECT new com.fdm.barbieBank.model.MonthlyDebitAndCredit(SUM(t.debit),SUM(t.credit),"
			+ "MONTH(t.transaction_date),YEAR(t.transaction_date)) "
			  + "FROM BankAccountTransactions AS t "
			  + "WHERE t.bankAccount= :bankAccount "
			  + "AND DATE(t.transaction_date) >= :startDate "
			  + "AND DATE(t.transaction_date) <= :endDate "
			  + "GROUP BY MONTH(t.transaction_date),YEAR(t.transaction_date) "
			  + "ORDER BY YEAR(t.transaction_date), MONTH(t.transaction_date) DESC"
			  )
	List<MonthlyDebitAndCredit> findByBankAccountSumDebitAndCreditByMonthAndYearClass(BankAccount bankAccount, Date startDate, Date endDate);
	
	
}
