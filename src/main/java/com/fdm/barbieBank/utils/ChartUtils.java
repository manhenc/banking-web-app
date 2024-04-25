package com.fdm.barbieBank.utils;

import java.util.ArrayList;
import java.util.List;

import com.fdm.barbieBank.model.BankAccount;
import com.fdm.barbieBank.model.ChartPoint;
import com.fdm.barbieBank.model.CreditCard;
import com.fdm.barbieBank.model.ChartLine;

public class ChartUtils {

	public static ChartLine getChartLine(List<String> monthlyLabels, List<ChartPoint>monthlyBalances ,String label, int colorPicker) {
	
		
		//For chart line points
		List<Double> chartPoints = getChartPoints(monthlyLabels, monthlyBalances);
		
		//For color
		String colorHexForAccount = getColorHex(colorPicker);
		
	
		//generate chartLine
		ChartLine chartLine = new ChartLine(label, chartPoints, colorHexForAccount, false);
		return chartLine;
    	
	}
	
	public static List<Double> getChartPoints(List<String> monthlyLabels, List<ChartPoint>monthlyBalances){
		List<Double> chartPoints = new ArrayList<Double>();
		
		//index - no of non-zero balances for that account
		int index = 0; 
		//max no of non-zero balances for that account
		int max = monthlyBalances.size();
		
		for(String label: monthlyLabels) {
			//case 1: not all non-zero chart points are saved in list yet
			if(index<max) {
				ChartPoint refChartPoint = monthlyBalances.get(index);
				
				//case 1a: chart point is same as label, meaning it belongs to this label for line
				if(refChartPoint.getMonthAndYear().equals(label)) {
						chartPoints.add(refChartPoint.getAmount());
						index++;
				}
						
				//case 1b: chart point is not same as label, no balance for this label
				else {
						chartPoints.add(0.00);
				}
			}
					
			//case 2:  all non-zero chart points are saved in list 
			else {
				chartPoints.add(0.00);
			}
		}
				
		return chartPoints;
	}
	
	public static String getCreditCardLabel(long creditCardNumber) {
		return  "-" + String.valueOf(creditCardNumber).substring(12, 16);
	}
	
	public static String getBankAccountLabel(long bankAccountNumber) {
		return  "-" + String.valueOf(bankAccountNumber).substring(8, 12);
	}
	
	public static String getColorHex(int colNum) {
		int num = colNum % 5;
		if(num == 0) {
			return "#ED7D31";
		}else if(num == 1) {
			return "#4472C4";
		}
		else if(num == 2) {
			return "#A5A5A5";
		}
		else if(num == 3) {
			return "#FFC000";
		}
		else if(num == 4) {
			return "#70AD47";
		}
		return "#FF69B4";
	}

}
