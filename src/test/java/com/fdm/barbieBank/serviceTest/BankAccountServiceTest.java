package com.fdm.barbieBank.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.repository.BankAccountRepository;
import com.fdm.barbieBank.service.BankAccountService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

	@Mock
	BankAccountRepository bankAccountRepository;
	
	@InjectMocks
	BankAccountService bankAccountService;
	
	@Test
	public void test_createBankAccount_returns_bankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setUser(user);
		
		when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
		
		BankAccount bankAccountCreated = bankAccountService.createBankAccount(bankAccount);
		
		assertEquals(bankAccountCreated.getAccountNumber(),439900212134L);
		verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
	
	}
	
	@Test
	public void test_displayListOfBankAccountsOfUser_returns_listOfBankAccountOfUser() {
	
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount1 = new BankAccount(439900212134L,0.00, date, null);
		BankAccount bankAccount2 = new BankAccount(439900321231L,0.00, date, null);
		bankAccount1.setUser(user);
		bankAccount2.setUser(user);
		
		when(bankAccountRepository.findByUser(user)).thenReturn(List.of(bankAccount1,bankAccount2));
		
		assertEquals(bankAccountService.displayListOfBankAccountsOfUser(user).size(),2);
	}

	@Test
	public void test_getTotalBalanceOfAllAccounts_returns_overallBalanceOfUser() {
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		when(bankAccountRepository.findSumOfAllBankAccountBalanceOfUser(1)).thenReturn(23.5);
		
		assertEquals(bankAccountService.getTotalBalanceOfAllAccounts(user), 23.5);
		
	}
	
	@Test
	public void test_getBankAccountByValidId_returnsBankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setBankAccountId(1);
		bankAccount.setUser(user);
		Optional<BankAccount> bankAccountOptionalExpected = Optional.of(bankAccount);
		
		long bankAccountId = 1;
		
		when(bankAccountRepository.findById(bankAccountId)).thenReturn(bankAccountOptionalExpected);
		Optional<BankAccount> bankAccountOptionalActual = bankAccountService.getBankAccountDetails(bankAccountId);
		assertEquals(bankAccountOptionalExpected, bankAccountOptionalActual);
		verify(bankAccountRepository, times(1)).findById(bankAccountId);
	}
	

	@Test
	public void test_getBankAccountByInvalidId_returnsNothing() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setBankAccountId(1);
		bankAccount.setUser(user);
		
		long bankAccountId = 2;
		
		when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.empty());
		Optional<BankAccount> bankAccountOptionalActual = bankAccountService.getBankAccountDetails(2);
		assertEquals(Optional.empty(), bankAccountOptionalActual);
		assertNotEquals(Optional.of(bankAccount),bankAccountOptionalActual);
		verify(bankAccountRepository, times(1)).findById(bankAccountId);
	}
	
	@Test
	public void test_depositAmount_increaseBalanceofBankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,32.00, date, null);
		bankAccount.setUser(user);
		when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
		
		BankAccount bankAccountUpdated = bankAccountService.depositBankAccount(bankAccount, 12.10);
		assertEquals(bankAccountUpdated.getBalance(),44.10);
		verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
		
	}
	
	@Test
	public void test_depositAmountForNewAccount_increaseBalanceofBankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setUser(user);
		when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
		
		BankAccount bankAccountUpdated = bankAccountService.depositBankAccount(bankAccount, 1000);
		assertEquals(bankAccountUpdated.getBalance(),1000.00);
		verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
		
	}
	
	@Test
	public void test_withdrawAmount_decreaseBalanceofBankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,32.00, date, null);
		bankAccount.setUser(user);
		when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
		
		BankAccount bankAccountUpdated = bankAccountService.withdrawBankAccount(bankAccount, 12.10);
		assertEquals(bankAccountUpdated.getBalance(),19.90);
		verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
		
	}
	
	@Test
	public void test_withdrawFullAccount_balanceofBankAccountBecomesZero() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,50.00, date, null);
		bankAccount.setUser(user);
		when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);
		
		BankAccount bankAccountUpdated = bankAccountService.withdrawBankAccount(bankAccount, 50);
		assertEquals(bankAccountUpdated.getBalance(),0.00);
		verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
		
	}
	
	@Test
	public void test_transferAmount_decreaseSrcBankBalance_increaseDestBankBalance() {
		Date srcDate = new Date();	
		User srcUser = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		User destUser = new User(2,"just","ken","randomken","welcome123", "14/12/1993","12345678", "blk 123", "fdm street", "123456", "test@gmail.com");
		Date destDate = new Date();	
		
		BankAccount srcBankAccount = new BankAccount(439900212134L,500.00, srcDate, null);
		srcBankAccount.setUser(srcUser);
		
		BankAccount destBankAccount = new BankAccount(439900212222L,15.00, destDate, null);
		destBankAccount.setUser(destUser);
		
		
		when(bankAccountRepository.save(srcBankAccount)).thenReturn(srcBankAccount);
		when(bankAccountRepository.save(destBankAccount)).thenReturn(destBankAccount);
		
		bankAccountService.transferFromAndToBankAccounts(srcBankAccount, destBankAccount, 10);
		assertEquals(srcBankAccount.getBalance(),490.00);
		assertEquals(destBankAccount.getBalance(),25.00);
		
		verify(bankAccountRepository, times(2)).save(any(BankAccount.class));
		
	}
	
	@Test
	public void test_getBankAccountByInvalidAccountNumber_returnsNothing() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setBankAccountId(1);
		bankAccount.setUser(user);
	
		
		when(bankAccountRepository.findByAccountNumber(234900212134L)).thenReturn(Optional.empty());
		Optional<BankAccount> bankAccountOptionalActual = bankAccountService.getBankAccountDetailsByAccountNumber(234900212134L);
		assertEquals(Optional.empty(), bankAccountOptionalActual);
		assertNotEquals(Optional.of(bankAccount),bankAccountOptionalActual);
		verify(bankAccountRepository, times(1)).findByAccountNumber(234900212134L);
	}
	
	@Test
	public void test_getBankAccountByValidAccountNumber_returnsBankAccount() {
		Date date = new Date();	
		User user = new User(1,"just","barbie","randombarbie","testing123", "12/3/2002","12345678", "blk 123", "fdm street", "123456", "random@gmail.com");
		
		BankAccount bankAccount = new BankAccount(439900212134L,0.00, date, null);
		bankAccount.setBankAccountId(1);
		bankAccount.setUser(user);
	
		
		when(bankAccountRepository.findByAccountNumber(439900212134L)).thenReturn(Optional.of(bankAccount));
		Optional<BankAccount> bankAccountOptionalActual = bankAccountService.getBankAccountDetailsByAccountNumber(439900212134L);
		assertEquals(Optional.of(bankAccount), bankAccountOptionalActual);
		verify(bankAccountRepository, times(1)).findByAccountNumber(439900212134L);
	}
	

}
