package com.fdm.barbieBank.utils;

public class BankAccountUtils {

	public static long getBankAccountNumber() {
		int first4digit = getRandomNumber(1000,9999);
		int mid4digit = getRandomNumber(1000,9999);
		int last4digit = getRandomNumber(1000,9999);
		long bankAccountNumber = 100000000L;
		bankAccountNumber *= first4digit;
		bankAccountNumber += (mid4digit * 10000);
		bankAccountNumber += last4digit;
		
		return bankAccountNumber;
		
		
	}

	public static int getRandomNumber(int min, int max) {
		return (int) (Math.random() * ((max - min) + 1)) + min;
	}
	
	
	

}
