package com.fdm.barbieBank.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.BankAccountTransactions;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardPaymentRequest;
import com.fdm.barbieBank.model.CreditCardTransactions;
import com.fdm.barbieBank.model.Currency;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.service.CreditCardTransactionsService;
import com.fdm.barbieBank.service.ForexDatabaseService;
import com.fdm.barbieBank.service.UserService;

@Controller
public class CreditCardTransactionsController {

	@Autowired
	private CreditCardTransactionsService creditCardTransactionsService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private UserService userService;

	@Autowired
	private ForexDatabaseService forexDbService;

	@GetMapping("/transactions/credit-card")
	public String goToCreditTransactionsPage(Model model,
			@RequestParam(required = false) Boolean insufficientLimitError,
			@RequestParam(required = false) Boolean wrongcvvError,
			@RequestParam(required = false) Boolean wrongExpirationError)
			throws StreamReadException, DatabindException, IOException {

		model.addAttribute("creditPaymentRequestForm", new CreditCardPaymentRequest());

		// populating list of currencies
		List<Currency> storedCurrencies = forexDbService.findListOfStoredUnroundedCurrencies();
		model.addAttribute("currencies", storedCurrencies);

		if (Boolean.TRUE.equals(insufficientLimitError)) {
			model.addAttribute("insufficientLimitError", "Insufficient Credit Card Limit!");
		}
		if (Boolean.TRUE.equals(wrongcvvError)) {
			model.addAttribute("wrongcvvError", "Incorrect CVV Code Entered!");
		}
		if (Boolean.TRUE.equals(wrongExpirationError)) {
			model.addAttribute("wrongExpirationError", "Incorrect Expiry Date Entered!");
		}
		String username = getUsername(); // Retrieve the username (user id) from Spring Security
		User user = userService.findVerifiedUser(username);
		List<CreditCard> userCards = creditCardService.getCardsByUserId(user.getUserID());
		model.addAttribute("creditCards", userCards);

		// initialising remaininglimit and balancedue
		model.addAttribute("remainingLimit", " ");

		return "creditTransactions"; // Make sure this matches your Thymeleaf template name
	}

	@PostMapping("/submitpayment")
	public String submitPayment(@RequestParam long cardId,
			@RequestParam int expirydatemonth,
			@RequestParam int expirydateyear,
			@RequestParam String cvv,
			@RequestParam String fromCurrency,
			@RequestParam BigDecimal amount,
			@RequestParam String merchant,
			@RequestParam int mcc, Model model) throws StreamReadException, DatabindException, IOException {

		CreditCard paymentCard = creditCardService.getCardByCardId(cardId);
		CreditCard toBillCard;
		// check if the card is a secondary card
		try {
			if (paymentCard.getPrimaryCardId() != null && paymentCard.getPrimaryCardId() != 0) {
				toBillCard = creditCardService.getCardByCardId(paymentCard.getPrimaryCardId());
			} else {
				toBillCard = paymentCard;
			}
		} catch (NullPointerException e) {
			toBillCard = paymentCard;
		}

		// validation check for Expiry Date
		if (expirydatemonth != (paymentCard.getExpiryDate().getMonthValue())
				|| expirydateyear != (paymentCard.getExpiryDate().getYear())) {
			return "redirect:/transactions/credit-card?wrongExpirationError=true";
		}

		// validation check for cvv
		if (!cvv.equals(paymentCard.getCvc())) {
			return "redirect:/transactions/credit-card?wrongcvvError=true";
		}

		// currency conversion if not in SGD
		BigDecimal convertedAmount = new BigDecimal(
				forexDbService.calculateCurrencyConversion(fromCurrency, "SGD", amount.toString()));

		// validation checks for limit display insufficient limit message
		if (toBillCard.getLimit().compareTo(convertedAmount) < 0
				|| paymentCard.getActualLimit().compareTo(convertedAmount) < 0) {
			return "redirect:/transactions/credit-card?insufficientLimitError=true"; // Return to the same view with the
																						// error message
		}

		// Date and time of transaction
		Date paymentDate = new Date();

		// Convert MCC to human readable format
		String transactionType = creditCardTransactionsService.getTransactionTypeByMCC(mcc);

		// Concatenated Description to be displayed in transaction list (can add on if
		// necessary)
		String transactionDescription = merchant + " -- " + fromCurrency + " " + amount.toString();

		Byte completedPayment = 1;
		// can replace this with the installment value
		// 0=unpaid
		// 1=paid
		// 2=partially paid

		CreditCardTransactions paymentTransaction = new CreditCardTransactions(paymentDate, transactionDescription,
				transactionType,
				convertedAmount, completedPayment, toBillCard);
		creditCardTransactionsService.saveTransaction(paymentTransaction);

		// update credit limit
		BigDecimal currentLimit = toBillCard.getLimit();
		currentLimit = currentLimit.subtract(convertedAmount);
		toBillCard.setLimit(currentLimit);
		// sBigDecimal currentBillUnpaid =
		creditCardService.registerNewCard(toBillCard);

		return "redirect:/transactions/credit-card";
	}

	@PostMapping("/transactions/credit-card")
	public String refreshCreditTransactions(@RequestParam long cardSelector,
			Model model) throws StreamReadException, DatabindException, IOException {
		String username = getUsername(); // Retrieve the username (user id) from Spring Security
		User user = userService.findVerifiedUser(username);

		// populating currencies in the payment form again
		List<Currency> storedCurrencies = forexDbService.findListOfStoredUnroundedCurrencies();
		model.addAttribute("currencies", storedCurrencies);

		// populating list of credit cards again
		List<CreditCard> userCards = creditCardService.getCardsByUserId(user.getUserID());
		model.addAttribute("creditCards", userCards);

		// initilising remaininglimit and balancedue
		model.addAttribute("remainingLimit", " ");

		if (cardSelector != 0) {
			// populating the currencies again
			model.addAttribute("currencies", storedCurrencies);
			CreditCard card = creditCardService.getCardByCardId(cardSelector);
			List<CreditCardTransactions> cardTransactions = creditCardTransactionsService
					.getTransactionsByCreditCard(card);
			model.addAttribute("creditCardTransactions", cardTransactions);
			model.addAttribute("cardSelector", cardSelector);

			// retrieve remaining card limit
			BigDecimal cardRemainingLimit = card.getLimit();
			model.addAttribute("remainingLimit", cardRemainingLimit.toString());

		}

		return "creditTransactions";
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
