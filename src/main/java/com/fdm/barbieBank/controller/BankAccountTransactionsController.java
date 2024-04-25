package com.fdm.barbieBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.BankAccountTransactions;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardTransactions;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.service.BankAccountService;
import com.fdm.barbieBank.service.BankAccountTransactionsService;
import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.service.CreditCardTransactionsService;
import com.fdm.barbieBank.service.UserService;

import java.util.List;

@Controller
public class BankAccountTransactionsController {

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountTransactionsService bankAccountTransactionsService;

	@GetMapping("/transactions/bank-account")
	public String goToBankTransactionsPage(Model model) {

		String username = getUsername(); // Retrieve the username (user id) from Spring Security
		User user = userService.findVerifiedUser(username);
		List<BankAccount> accounts = bankAccountService.displayListOfBankAccountsOfUser(user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("currentBalance", "0");
		model.addAttribute("totalCount", "0");
		model.addAttribute("selectedAcc", 0L);

		return "bankTransactions";
	}

	@PostMapping("/transactions/bank-account")
	public String refreshBankTransactions(@RequestParam long accountSelector, Model model) {
		//		System.out.println(accountSelector);
		String username = getUsername(); // Retrieve the username (user id) from Spring Security
		User user = userService.findVerifiedUser(username);
		List<BankAccount> accounts = bankAccountService.displayListOfBankAccountsOfUser(user);
		model.addAttribute("accounts", accounts);

		if (accountSelector != 0) {
			BankAccount account = bankAccountService.getBankAccountDetails(accountSelector).get();
			
			List<BankAccountTransactions> accountTransactions = bankAccountTransactionsService.getTransactionsByBankAccount(account);
			
			String balanceString = String.valueOf(account.getBalance());
			String transactionCount = String.valueOf(accountTransactions.size());
			model.addAttribute("bankAccountTransactions", accountTransactions);
			model.addAttribute("currentBalance", balanceString);
			model.addAttribute("totalCount", transactionCount );
			model.addAttribute("selectedAcc", account.getAccountNumber());
		}else {
			model.addAttribute("currentBalance", "0");
			model.addAttribute("totalCount", "0");
			model.addAttribute("selectedAcc", 0L);
		}

		return "bankTransactions";
	}

	public String getUsername() {
		// get user name from spring security
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();

			return username;
		} else {
			String username = principal.toString();
			return username;

		}

	}

}
