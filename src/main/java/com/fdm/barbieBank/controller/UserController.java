package com.fdm.barbieBank.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.barbieBank.service.BankAccountService;
import com.fdm.barbieBank.service.BankAccountTransactionsService;
import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.service.CreditCardTransactionsService;
import com.fdm.barbieBank.service.UserService;
import com.fdm.barbieBank.utils.BankAccountUtils;

import com.fdm.barbieBank.utils.ChartUtils;
import com.fdm.barbieBank.utils.DateUtils;

import com.fdm.barbieBank.utils.InvalidCurrentPasswordException;
import com.fdm.barbieBank.security.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.ChartPoint;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.model.ChartLine;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private BankAccountTransactionsService bankAccountTransactionsService;
	
	@Autowired
	private CreditCardTransactionsService creditCardTransactionsService;
	
	@GetMapping("/dashboard")
	public String goToDashboardPage(Model model, HttpSession session) {

		//to retrieve user details who logged in
		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();
		User user = userService.findVerifiedUser(currentUser);
		model.addAttribute("userFromModel", user);

		//to display bank accounts and their balance
		List<BankAccount> userBankAccounts = bankAccountService.displayListOfBankAccountsOfUser(user);
		model.addAttribute("bankAccounts", userBankAccounts);

		//total balance
		double totalBalance = bankAccountService.getTotalBalanceOfAllAccounts(user);
		model.addAttribute("totalBalance", totalBalance);

		// credit cards of user
		List<CreditCard> userCards = creditCardService.getCardsByUserId(user.getUserID());
		model.addAttribute("cards", userCards);

		//bill to pay
		double totalBill = creditCardService.getTotalBillsOfAllAccounts(user);
		model.addAttribute("totalUnpaidBills", totalBill);
		
		// --- FOR CHART --- 
		//to get start and end date to retrieve monthly balance for chart
		Date[] dateRange = DateUtils.getStartAndEndDateMonthly(3);
    	Date startDate = dateRange[0];
    	Date endDate = dateRange[1];
    	
    	List<String> monthAndYearLabels = DateUtils.getMonthAndYearLabels(startDate, endDate);
    	model.addAttribute("chartLabels", monthAndYearLabels);
    	
    	// -- Bank Account Chart --
    	//will store data for entire bank account chart
    	List<ChartLine> bankAccountChart = new ArrayList<ChartLine>();
    	
    	//label for x-axis (month and year)
    	int noOfBankAccountDone = 0;

    	//each bank account = 1 chart line
		for(BankAccount bankAccount: userBankAccounts) {
			
			
			//sorted from latest to earliest hence reverse to start from earliest
			List<ChartPoint> monthlyBalanceForAccount = bankAccountTransactionsService.getBankAccountChart(bankAccount, startDate, endDate);
		
			Collections.reverse(monthlyBalanceForAccount);
			
			//for label
			String bankAccountLabel = ChartUtils.getBankAccountLabel(bankAccount.getAccountNumber());
			
			//generate chart line accordingly and add to chart
			ChartLine chartLine = ChartUtils.getChartLine(monthAndYearLabels, monthlyBalanceForAccount, bankAccountLabel, noOfBankAccountDone);
			bankAccountChart.add(chartLine);
			
			//this used to determine the line color
			noOfBankAccountDone++;
			
		}
		
		// -- Credit Card Chart --
		//will store data for entire credit card chart
    	List<ChartLine> creditCardChart = new ArrayList<ChartLine>();
    	
    	//label for x-axis (month and year)
    	int noOfCreditCardDone = 0;

    	//each credit card = 1 chart line
		for(CreditCard creditCard: userCards) {
		
			//sorted from earliest to latest already
			List<ChartPoint> monthlySpendingForCard = creditCardTransactionsService.getCreditCardChart(creditCard, startDate, endDate);
			
			//for label
			String creditCardLabel = ChartUtils.getCreditCardLabel(creditCard.getCardNumber());
			
			//generate chart line accordingly and add to chart
			ChartLine chartLine = ChartUtils.getChartLine(monthAndYearLabels, monthlySpendingForCard, creditCardLabel, noOfCreditCardDone);
			creditCardChart.add(chartLine);
			
			//this used to determine the line color
			noOfCreditCardDone++;
			
		}
		
	
		//to ensure it can be in json and processed by the chart in js for frontend
		ObjectMapper mapper = new ObjectMapper();
		String bankAccountChartString = null;
		String creditCardChartString = null;
		
		try {
			bankAccountChartString = mapper.writeValueAsString(bankAccountChart);
			creditCardChartString = mapper.writeValueAsString(creditCardChart);
			model.addAttribute("bankAccountChartLines", bankAccountChartString);
			model.addAttribute("creditCardChartLines", creditCardChartString);
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("bankAccountChartLines", "{}");
			model.addAttribute("creditCardChartLines", "{}");
		}
		
		return "dashboard";

	}
	
	@PostMapping("/changePassword")
	public String changePassword(Locale locale, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
		
		User user = userService.findVerifiedUser(SecurityContextHolder.getContext().getAuthentication().getName());
	
		if (!userService.checkIfValidCurrentPassword(user, currentPassword)) {
	        
			throw new InvalidCurrentPasswordException();
	    
		}
	    
		userService.changeCurrentPassword(user, newPassword);
	    
	    return "login";
	}
	
	@GetMapping("/chatbot")
	public String goToChatbotPage() {

		return "chatbot";

	}

	@GetMapping("/index")
	public String goToIndexPage() {

		return "index";

	}

	@GetMapping("/login")
	public String goToLoginPage() {

		return "login";

	}

	@GetMapping("/logout")
	public String goToLogoutPage(HttpSession session) {

		session.invalidate();
		return "redirect:/";

	}

	@GetMapping("/profile")
	public String goToProfilePage(Model model, HttpSession session) {

		String currentUser = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();
		User user = userService.findVerifiedUser(currentUser);
		model.addAttribute("userFromModel", user);
		return "profile";

	}

	@GetMapping("/registerUser")
	public String goToUserRegistrationPage(Model model) {

		model.addAttribute("user", new User());
		return "registerUser";

	}

	@PostMapping("/registerUser")
	public String registerUser(User user) {

		if (userService.registerNewUser(user)) {

			// create bank account for logged in user
			Date openedDate = new Date();
			long bankAccountNumber = BankAccountUtils.getBankAccountNumber();
			BankAccount bankAccount = new BankAccount(bankAccountNumber, 0.00, openedDate, null);
			bankAccount.setUser(user);
			bankAccountService.createBankAccount(bankAccount);

			return "redirect:/login";

		} else {

			return "registerUser";

		}

	}

}
