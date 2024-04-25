package com.fdm.barbieBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdm.barbieBank.service.BankAccountService;
import com.fdm.barbieBank.service.BankAccountTransactionsService;
import com.fdm.barbieBank.service.UserService;
import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.BankAccountTransactions;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.security.UserPrincipal;
import com.fdm.barbieBank.utils.BankAccountUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BankAccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountService bankAccountService;
	
	@Autowired
	private BankAccountTransactionsService bankAccountTransactionsService; 

	@GetMapping("/bank-account")
	public String goToBankAccountPage(Model model) {
		// retrieve user
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);

		// get list of bank accounts opened by logged in user
		List<BankAccount> userBankAccounts = bankAccountService.getOpenBankAccountsOfUser(user);
		List<BankAccount> closedBankAccounts = bankAccountService.getClosedBankAccountsOfUser(user);
		
		model.addAttribute("bankAccounts", userBankAccounts);
		model.addAttribute("closedBankAccounts", closedBankAccounts);

		return "bankAccount";
	}

	@PostMapping("/bank-account")
	public String openBankAccount() {
		// retrieve user
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		long bankAccountNumber = BankAccountUtils.getBankAccountNumber();

		// current timestamp for bank opened
		Date date = new Date();

		BankAccount bankAccount = new BankAccount(bankAccountNumber, 0.00, date, null);
		bankAccount.setUser(user);

		// create bank account for logged in user
		bankAccountService.createBankAccount(bankAccount);

		return "redirect:/bank-account";
	}
	
	@GetMapping("/deposit")
	public String goToDepositPage(Model model) {
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		List<BankAccount> userBankAccounts = bankAccountService.getOpenBankAccountsOfUser(user);

		model.addAttribute("bankAccounts", userBankAccounts);
		return "depositbankAccount";
	}
	
	@PostMapping("/deposit")
	public String depositToBankAccount(@RequestParam long bankAccountId, @RequestParam double depositAmount) {
		Optional<BankAccount> bankAccountOptional = bankAccountService.getBankAccountDetails(bankAccountId);
		BankAccount bankAccount = bankAccountOptional.get();
		bankAccountService.depositBankAccount(bankAccount, depositAmount);
		Date date = new Date();
		BigDecimal credit = new BigDecimal(0);
		BigDecimal debit = new BigDecimal(depositAmount);
		BankAccountTransactions newTransaction = new BankAccountTransactions(date, "Deposit", credit, debit, bankAccount);
		bankAccountTransactionsService.saveTransaction(newTransaction);
		return "redirect:/bank-account";
	}
	
	@GetMapping("/withdraw")
	public String goToWithdrawPage(Model model) {
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		List<BankAccount> userBankAccounts = bankAccountService.getOpenBankAccountsOfUser(user);

		model.addAttribute("bankAccounts", userBankAccounts);		
		return "withdrawbankAccount";
	}
	
	@PostMapping("/withdraw")
	public String withdrawFromBankAccount(@RequestParam long bankAccountId, @RequestParam double withdrawAmount) {
		Optional<BankAccount> bankAccountOptional = bankAccountService.getBankAccountDetails(bankAccountId);
		BankAccount bankAccount = bankAccountOptional.get();
		bankAccountService.withdrawBankAccount(bankAccount, withdrawAmount);
		Date date = new Date();
		BigDecimal credit = new BigDecimal(withdrawAmount);
		BigDecimal debit = new BigDecimal(0);
		BankAccountTransactions newTransaction = new BankAccountTransactions(date, "Withdraw", credit, debit, bankAccount);
		bankAccountTransactionsService.saveTransaction(newTransaction);
		return "redirect:/bank-account";
	}
	
	@GetMapping("/transfer")
	public String goToTransferPage(Model model) {
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		List<BankAccount> userBankAccounts = bankAccountService.getOpenBankAccountsOfUser(user);

		model.addAttribute("bankAccounts", userBankAccounts);
		return "transferbankAccount";
	}
	

	@PostMapping("/transfer")
	public String transferAmount(@RequestParam long srcBankAccountId,  @RequestParam long destAccountNumber,  @RequestParam double transferAmount, 
			RedirectAttributes redirectAttrs) {
		Optional<BankAccount> bankAccountOptionalSrc = bankAccountService.getBankAccountDetails(srcBankAccountId);
		Optional<BankAccount> bankAccountOptionalDest = bankAccountService.getBankAccountDetailsByAccountNumber(destAccountNumber);
		
		if(bankAccountOptionalDest.isEmpty()) {
			redirectAttrs.addFlashAttribute("errorMsg", "Bank Account Not Found!");
			return "redirect:/transfer";
		}
		
		if(bankAccountOptionalDest.get().getBankAccountId() == srcBankAccountId) {
			redirectAttrs.addFlashAttribute("errorMsg", "Source and destination bank account number cannot be the same!");
			return "redirect:/transfer";
		}
		
		
		
		else {
			BankAccount srcBankAccount = bankAccountOptionalSrc.get();
			BankAccount destBankAccount = bankAccountOptionalDest.get();
			bankAccountService.transferFromAndToBankAccounts(srcBankAccount, destBankAccount, transferAmount);
			Date date = new Date();
			
			String srcBankAccNumber = String.valueOf(srcBankAccount.getAccountNumber());
			String destBankAccNumber = String.valueOf(destAccountNumber);
			
			BigDecimal creditSource = new BigDecimal(transferAmount);
			BigDecimal debitSource = new BigDecimal(0);
			BankAccountTransactions sourceTransaction = new BankAccountTransactions(date, "Transfer to " + destBankAccNumber, creditSource, debitSource, srcBankAccount);
			bankAccountTransactionsService.saveTransaction(sourceTransaction);
			
			BigDecimal creditDest = new BigDecimal(0);
			BigDecimal debitDest = new BigDecimal(transferAmount);
			BankAccountTransactions destTransaction = new BankAccountTransactions(date, "Transfer from " + srcBankAccNumber, creditDest, debitDest, destBankAccount);
			bankAccountTransactionsService.saveTransaction(destTransaction);
			return "redirect:/bank-account";
		}
		
	}
	
	@GetMapping("/close-account")
	public String goToCloseAccountPage(Model model) {
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		List<BankAccount> userBankAccounts = bankAccountService.getOpenBankAccountsOfUser(user);

		model.addAttribute("bankAccounts", userBankAccounts);
		return "closebankAccount";
	}
	
	@PostMapping("/close-account")
	public String closeBankAccount(@RequestParam long srcBankAccountId, @RequestParam long destBankAccountId, @RequestParam String password,
			RedirectAttributes redirectAttrs) {
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		User user = userService.findVerifiedUser(currentUser);
		
		//same bank account error
		if(srcBankAccountId == destBankAccountId) {
			redirectAttrs.addFlashAttribute("errorMsg", "Cannot transfer funds to same closing account");
			return "redirect:/close-account";
		}
		
		//wrong password
		if(!userService.checkIfValidCurrentPassword(user, password)) {
			redirectAttrs.addFlashAttribute("errorMsg", "Incorrect password!");
			return "redirect:/close-account";
		}
		
		
		//just withdraw money and close account
		if(destBankAccountId == 0) {
			BankAccount closingBankAccount = bankAccountService.getBankAccountDetails(srcBankAccountId).get();
			Date closingDate = new Date();
			bankAccountService.closeAccount(closingBankAccount, closingDate);
			BigDecimal balance = new BigDecimal(0.00);
			BankAccountTransactions closedTransaction = new BankAccountTransactions(closingDate, "Closed Account", balance, balance, closingBankAccount);
			bankAccountTransactionsService.saveTransaction(closedTransaction);
		}
		
		//transfer money from and to and then close
		else {
			BankAccount closingBankAccount = bankAccountService.getBankAccountDetails(srcBankAccountId).get();
			Date closingDate = new Date();
			double amount = closingBankAccount.getBalance();
			BigDecimal balance = BigDecimal.valueOf(amount);
			BigDecimal zero = new BigDecimal(0.00);
			
			BankAccount transfereeBankAccount = bankAccountService.getBankAccountDetails(destBankAccountId).get();
			bankAccountService.closeAccount(closingBankAccount, transfereeBankAccount, closingDate);
	
			BankAccountTransactions closedTransaction = new BankAccountTransactions(closingDate, "Closed Account", balance, zero, closingBankAccount);
			bankAccountTransactionsService.saveTransaction(closedTransaction);
			BankAccountTransactions transferTransaction = new BankAccountTransactions(closingDate, "Funds from closed Account " + closingBankAccount.getAccountNumber(), 
					zero, balance, transfereeBankAccount);
			bankAccountTransactionsService.saveTransaction(transferTransaction);
		}
		
		return "redirect:/bank-account";
	}

}
