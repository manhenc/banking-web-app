package com.fdm.barbieBank.controller;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardTransactions;

import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.service.BankAccountService;
import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.service.CreditCardTransactionsService;
import com.fdm.barbieBank.service.InstallmentItemService;
import com.fdm.barbieBank.service.UserService;
import com.fdm.barbieBank.utils.CreditCardGenerator;

@Controller
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CreditCardTransactionsService creditCardTransactionsService;

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

    public void updateBillUnpaidForAllCards(long userid) {
        // updates bill unpaid for all cards
        List<CreditCard> allCreditCards = creditCardService.getCardsByUserId(userid);

        for (CreditCard creditCard : allCreditCards) {
            BigDecimal totalUnpaid = calculateTotalUnpaidForCard(creditCard);
            creditCard.setBillUnpaid(totalUnpaid);
            creditCardService.registerNewCard(creditCard); // Update the credit card in the database
        }
    }

    private BigDecimal calculateTotalUnpaidForCard(CreditCard creditCard) {
        // first pull all transaction
        List<CreditCardTransactions> creditTrans = creditCardTransactionsService
                .getTransactionsByCreditCard(creditCard);
        LocalDate startOfTime = LocalDate.of(1900, 1, 1);
        LocalDate endOfLastMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);
        // get all unpaid from this month minus one day to the start of time
        BigDecimal totalUnpaid = creditTrans.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransaction_date().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return transactionDate.isBefore(endOfLastMonth.plusDays(1))
                            && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0;
                })
                .map(CreditCardTransactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // get the paid transaction add
        BigDecimal totalPaid = creditTrans.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransaction_date().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return transactionDate.isAfter(startOfTime)
                            && transaction.getAmount().compareTo(BigDecimal.ZERO) < 0;
                })
                .map(CreditCardTransactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // update each card
        totalUnpaid = totalUnpaid.add(totalPaid);
        return totalUnpaid;
    }

    @GetMapping("/credit-card")
    public String showUserCards(Model model) {
        String username = getUsername(); // Retrieve the username (user id) from Spring Security
        User user = userService.findVerifiedUser(username);
        List<CreditCard> userCards = creditCardService.getCardsByUserId(user.getUserID());
        updateBillUnpaidForAllCards(user.getUserID());
        model.addAttribute("cards", userCards);
        return "creditCard"; // Thymeleaf template name
    }

    @GetMapping("/credit-card/{cardId}")
    public String showCardDetails(@PathVariable("cardId") Long cardId, Model model,
            @RequestParam(required = false) Boolean negativeBalanceError) {
        // Fetch the card details using the cardId
        CreditCard card = creditCardService.getCardByCardId(cardId);
        if (Boolean.TRUE.equals(negativeBalanceError)) {
            model.addAttribute("negativeBalanceError", "Negative credit card balance");
        }
        // Add the card details to the model to display on the card details page
        model.addAttribute("card", card);
        List<CreditCard> secondaryCard = creditCardService.getCardsByPrimaryCardId(cardId);
        model.addAttribute("secondary_cards", secondaryCard);
        return "selectedCreditCard";
    }

    @GetMapping("/createCard")
    public String showCreateCards(Model model) {

        String username = getUsername(); // Retrieve the username (user id) from Spring Security
        User user = userService.findVerifiedUser(username);
        String generatedCardNumber;
        do {
            generatedCardNumber = CreditCardGenerator.generateRandomCreditCardNumber();
        } while (creditCardService.checkCardByCardNumber(Long.parseLong(generatedCardNumber)));
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) (random.nextInt(10) + '0'); // Generates a random digit character (0-9)
            sb.append(randomChar);
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(Long.parseLong(generatedCardNumber));
        creditCard.setIssueDate(LocalDateTime.now());
        creditCard.setCvc(sb.toString()); // Set the CVC
        creditCard.setLimit(new BigDecimal("1000"));
        creditCard.setActualLimit(new BigDecimal("1000")); // Set the limit
        creditCard.setBillUnpaid(BigDecimal.ZERO);
        creditCard.setExpiryDate(LocalDateTime.now().plusYears(3)); // Set expiry date
        creditCard.setUserId(user.getUserID());
        model.addAttribute("card", creditCard);
        return "newCreditCard";
    }

    @PostMapping("/generate-card")
    public String generateCard(@ModelAttribute("card") CreditCard creditCard) {

        String username = getUsername(); // Retrieve the username (user id) from Spring Security
        User user = userService.findVerifiedUser(username);
        System.out.println(user.getUserID());
        creditCardService.registerNewCard(creditCard);
        System.out.println("successfully created card");
        return "redirect:/credit-card";
    }

    @PostMapping("/update-credit-card-limit")
    public String updateCardLimit(@RequestParam Long cardId, @RequestParam BigDecimal newLimit, Model model) {
        CreditCard card = creditCardService.getCardByCardId(cardId);
        // change balance according to whats left
        BigDecimal difference = newLimit.subtract(card.getActualLimit());

        creditCardService.registerNewCard(card);
        // null checker
        if (difference != null) {
            BigDecimal newBalance = card.getLimit().add(difference);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                // If the new balance is negative, set an error message in the model

                return "redirect:/credit-card/" + cardId + "?negativeBalanceError=true";

            }
            card.setLimit(newBalance);
            // set new actual limit
            card.setActualLimit(newLimit);
            creditCardService.registerNewCard(card);

        }
        return "redirect:/credit-card";

    }

    @PostMapping("/delete-card")
    public String deleteCard(@RequestParam("cardId") Long cardId, Model model) {
        creditCardService.deleteCardById(cardId);
        return "redirect:/credit-card"; // Redirect to the credit card page after deletion
    }

    @GetMapping("/show-bill")
    public String showBillsPage(@RequestParam(name = "cardId") long cardId, Model model) {

        LocalDate today = LocalDate.now();
        LocalDate startOfThisMonth = today.withDayOfMonth(1);
        LocalDate startOfLastMonth = startOfThisMonth.minusMonths(1);
        LocalDate startOfTime = LocalDate.of(1900, 1, 1);
        LocalDate endOfLastMonth = today.withDayOfMonth(1).minusDays(1);
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        model.addAttribute("card", creditCard);
        // Retrieve all transaction last month from credit trans
        List<CreditCardTransactions> creditTrans = creditCardTransactionsService
                .getTransactionsByCreditCard(creditCard);

        List<CreditCardTransactions> transactionsInRange = creditTrans.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransaction_date().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return !transactionDate.isBefore(startOfLastMonth) && !transactionDate.isAfter(endOfLastMonth);
                })
                .collect(Collectors.toList());
        model.addAttribute("creditCardTransactions", transactionsInRange);
        // get total amount

        // negative transaction is paid

        LocalDate endOfThisMonth = today;

        List<CreditCardTransactions> negativeTransactionsThisMonth = creditTrans.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransaction_date().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return transactionDate.isAfter(startOfTime) // Filter for this month
                            && transactionDate.isBefore(endOfThisMonth.plusDays(1)) // Filter for this month
                            && transaction.getAmount().compareTo(BigDecimal.ZERO) < 0; // Include only negative
                                                                                       // transactions
                })
                .collect(Collectors.toList());

        model.addAttribute("paidCreditCardTransactions", negativeTransactionsThisMonth);
        BigDecimal totalAmountDue = transactionsInRange.stream()
                .map(CreditCardTransactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountPaid = negativeTransactionsThisMonth.stream()
                .map(CreditCardTransactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalAmountDue = totalAmountDue.add(totalAmountPaid);

        /// roll over transactions
        List<CreditCardTransactions> rollOverTransaction = creditTrans.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getTransaction_date().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return !transactionDate.isBefore(startOfTime) && !transactionDate.isAfter(startOfLastMonth);
                })
                .collect(Collectors.toList());
        BigDecimal rollOverAmount = rollOverTransaction.stream()
                .map(CreditCardTransactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalAmountDue = totalAmountDue.add(rollOverAmount);
        model.addAttribute("rollover", rollOverAmount);
        model.addAttribute("rolloverDate", startOfLastMonth);
        model.addAttribute("totalAmountDue", totalAmountDue);

        // model.addAttribute("roll over", rollover);

        return "showBills";
    }

    @GetMapping("/pay-bill")
    public String showPayBillsPage(@RequestParam(name = "cardId") long cardId, Model model) {
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        model.addAttribute("card", creditCard);
        String username = getUsername(); // Retrieve the username (user id) from Spring Security
        User user = userService.findVerifiedUser(username);
        List<BankAccount> userBankAccounts = bankAccountService.displayListOfBankAccountsOfUser(user);
        model.addAttribute("bankAccounts", userBankAccounts);

        return "payBills";
    }

    @PostMapping("/pay-bill")
    public String updateBillsUnpaid(@RequestParam Long cardId, @RequestParam BigDecimal paid,
            @RequestParam long srcBankAccountId) {
        System.out.println(srcBankAccountId);
        // withdraw from bank
        Optional<BankAccount> bankAccountOptional = bankAccountService.getBankAccountDetails(srcBankAccountId);
        BankAccount bankAccount = bankAccountOptional.get();
        double withdrawAmount = paid.doubleValue();
        bankAccountService.withdrawBankAccount(bankAccount, withdrawAmount);

        // update credit card
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        BigDecimal billsUnpaid = creditCard.getBillUnpaid();
        billsUnpaid = billsUnpaid.subtract(paid);
        creditCard.setBillUnpaid(billsUnpaid);
        creditCard.setReward((int) Math.round(paid.intValue() / 100.0));
        creditCardService.registerNewCard(creditCard);

        // create negative transaction
        CreditCardTransactions transactions = new CreditCardTransactions();
        transactions.setAmount(paid.negate());
        transactions.setDescription("Payment");
        transactions.setType("Credit card Payment");
        transactions.setCreditCard(creditCard);
        transactions.setTransaction_date(new Date());
        creditCardTransactionsService.saveTransaction(transactions);
        return "redirect:/credit-card";
    }

    @GetMapping("/check-reward")
    public String showRewardPage(@RequestParam(name = "cardId") long cardId, Model model) {
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        model.addAttribute("card", creditCard);
        return "rewardPage";
    }

    @PostMapping("/redeem")
    public String redeemReward(@RequestParam(name = "cardId") long cardId) {
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        BigDecimal reward = new BigDecimal("1");
        int rewardPoint = creditCard.getReward();
        rewardPoint = rewardPoint - 10;
        creditCard.setReward(rewardPoint);
        creditCardService.registerNewCard(creditCard);
        CreditCardTransactions transactions = new CreditCardTransactions();
        transactions.setAmount(reward.negate());
        transactions.setDescription("Reward redeem");
        transactions.setType("Redeem reward");
        transactions.setCreditCard(creditCard);
        transactions.setTransaction_date(new Date());
        creditCardTransactionsService.saveTransaction(transactions);
        return "redirect:/credit-card";
    }

    @GetMapping("/create-secondary-card")
    public String showCreateSecondaryCards(@RequestParam(name = "cardId") long cardId, Model model) {

        String username = getUsername(); // Retrieve the username (user id) from Spring Security
        User user = userService.findVerifiedUser(username);
        String generatedCardNumber;
        do {
            generatedCardNumber = CreditCardGenerator.generateRandomCreditCardNumber();
        } while (creditCardService.checkCardByCardNumber(Long.parseLong(generatedCardNumber)));
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) (random.nextInt(10) + '0'); // Generates a random digit character (0-9)
            sb.append(randomChar);
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(Long.parseLong(generatedCardNumber));
        creditCard.setIssueDate(LocalDateTime.now());
        creditCard.setCvc(sb.toString()); // Set the CVC
        creditCard.setLimit(new BigDecimal("1000"));
        creditCard.setActualLimit(new BigDecimal("1000"));// Set the limit
        creditCard.setBillUnpaid(BigDecimal.ZERO);
        creditCard.setExpiryDate(LocalDateTime.now().plusYears(3)); // Set expiry date
        creditCard.setUserId(user.getUserID());
        creditCard.setPrimaryCardId(cardId);
        model.addAttribute("card", creditCard);
        return "createSecondaryCard";
    }
}
