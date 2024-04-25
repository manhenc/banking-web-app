package com.fdm.barbieBank.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.User;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

	@Query(value = "SELECT SUM(b.balance) FROM bank_account b WHERE b.account_holder_user = ?1", nativeQuery = true)
	double findSumOfAllBankAccountBalanceOfUser(long userId);

	List<BankAccount> findByUser(User user);
	
	List<BankAccount> findByUserAndDateClosedIsNull(User user);
	
	List<BankAccount> findByUserAndDateClosedIsNotNull(User user);
	
	Optional<BankAccount> findByAccountNumber(long accountNumber);
}
