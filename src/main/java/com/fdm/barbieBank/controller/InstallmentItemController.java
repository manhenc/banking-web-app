package com.fdm.barbieBank.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Random;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.CreditCardTransactions;
import com.fdm.barbieBank.model.InstallmentItem;
import com.fdm.barbieBank.model.User;
import com.fdm.barbieBank.service.CreditCardService;
import com.fdm.barbieBank.service.CreditCardTransactionsService;
import com.fdm.barbieBank.service.InstallmentItemService;
import com.fdm.barbieBank.service.UserService;

@Controller
public class InstallmentItemController {
    @Autowired
    private CreditCardTransactionsService creditCardTransactionsService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private InstallmentItemService installmentItemService;

    @GetMapping("/check-installment")
    public String showInstallmentPage(@RequestParam(name = "cardId") long cardId, Model model) {
        // Fetch the credit card by cardId and pass it to the view
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        List<InstallmentItem> installmentItems = installmentItemService.getInstallmentItemByCard(cardId);
        model.addAttribute("installmentItems", installmentItems);
        model.addAttribute("card", creditCard);

        return "checkInstallment";
    }

    @GetMapping("/add-installment")
    public String showAddTransactionForm(@RequestParam(name = "cardId") long cardId, Model model) {
        // Fetch the credit card by cardId and pass it to the view
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        System.out.println(creditCard.getCardId());
        InstallmentItem installmentItem = new InstallmentItem();
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("installmentItem", installmentItem);
        return "addInstallment";
    }

    @PostMapping("/add-installment") // Map the POST request to this method
    public String addInstallment(@ModelAttribute InstallmentItem installmentItem, Model model,
            @RequestParam(name = "cardId") long cardId) {
        System.out.println(cardId);
        CreditCard creditCard = creditCardService.getCardByCardId(cardId);
        installmentItem.setCreditCard(creditCard);

        LocalDateTime transactionDate = installmentItem.getTransactionDate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        int monthsDifference = (currentDateTime.getYear() - transactionDate.getYear()) * 12
                + currentDateTime.getMonthValue() - transactionDate.getMonthValue();
        installmentItem.setMonthsPaid(monthsDifference);
        BigDecimal amountUnpaid = installmentItem.getAmountUnpaid();
        // credit card services first to change limit
        BigDecimal creditCardLimitCurrent = creditCard.getLimit();
        creditCardLimitCurrent = creditCardLimitCurrent.subtract(amountUnpaid);
        creditCard.setLimit(creditCardLimitCurrent);
        creditCardService.registerNewCard(creditCard);

        // continue with the update
        BigDecimal amountPerMonth = amountUnpaid.divide(BigDecimal.valueOf(installmentItem.getMonths()), 2,
                RoundingMode.HALF_UP);
        BigDecimal amountPaid = amountPerMonth.multiply(BigDecimal.valueOf(monthsDifference)); // Multiply to get total
        amountUnpaid = amountUnpaid.subtract(amountPaid);
        installmentItem.setAmountUnpaid(amountUnpaid);
        installmentItemService.addInstallment(installmentItem); // payment

        // installment add into bills
        for (int monthOffset = 0; monthOffset < monthsDifference; monthOffset++) {
            LocalDateTime transDate = currentDateTime.minusMonths(monthsDifference - monthOffset);
            CreditCardTransactions transactions = new CreditCardTransactions();
            transactions.setAmount(amountPerMonth);
            transactions.setDescription(String.valueOf(installmentItem.getItemId()));
            transactions.setType("Installment Payment");
            transactions.setPaid((byte) 0); // Mark as paid since these are past payments
            transactions.setCreditCard(creditCard);
            Instant instant = transDate.with(TemporalAdjusters.lastDayOfMonth()).atZone(ZoneId.systemDefault())
                    .toInstant();
            Date transactionDateDate = Date.from(instant);
            transactions.setTransaction_date(transactionDateDate);
            System.out.println("saving transac");
            creditCardTransactionsService.saveTransaction(transactions);
        }

        return "redirect:/credit-card";
    }

}
