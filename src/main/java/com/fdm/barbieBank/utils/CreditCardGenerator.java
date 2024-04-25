package com.fdm.barbieBank.utils;

import java.util.Random;

// Thank you Luhn algorithm
public class CreditCardGenerator {

    public static String generateRandomCreditCardNumber() {
        Random random = new Random();

        StringBuilder cardNumber = new StringBuilder("4"); // Start with 4 for Visa cards
        for (int i = 0; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }

        cardNumber.append(calculateLuhnDigit(cardNumber.toString()));

        return cardNumber.toString();
    }

    private static int calculateLuhnDigit(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - sum % 10) % 10;
    }
}
