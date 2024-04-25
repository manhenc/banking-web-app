package com.fdm.barbieBank.utils;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateUtils {

	private static String [] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov" , "Dec"};
	
	
	public static String getMonthInString(int month) {
		try {
			return monthArray[month - 1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "none";
		}
	}
	
	public static Date[] getStartAndEndDateMonthly(int monthMargin){
		Date[] dateRange = new Date[2];
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int endYear = calendar.get(Calendar.YEAR);
		int endMonth = calendar.get(Calendar.MONTH) + 1;
		int endDay = calendar.getActualMaximum(Calendar.DATE);		
		
		int startYear = (endMonth <=2 ) ? endYear - 1 : endYear;
		int startMonth =  (endMonth <= 2) ? (endMonth - monthMargin + 1) + 12 : (endMonth - monthMargin + 1);
		int startDay = 1;
		
		dateRange[0] = getDate(startYear, startMonth, startDay);
		dateRange[1] = getDate(endYear, endMonth, endDay);

		return dateRange;
	}
	
	public static Date getDate(int year, int month, int day) {
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");  
		
		String yearString = String.valueOf(year);
		String dayString = String.format("%02d" , day);
		String monthString = String.format("%02d", month);
		
		String dateString = yearString + "-" + monthString + "-" + dayString;
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
		
			return new Date();
		}
	}
	
	public static List<String> getMonthAndYearLabels(Date startDate, Date endDate) {
		
		//start month
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		int startMonth =  startCalendar.get(Calendar.MONTH) + 1;
		int startYear = startCalendar.get(Calendar.YEAR);
		
		//end month
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		int endMonth =  endCalendar.get(Calendar.MONTH) + 1;
		int endYear = endCalendar.get(Calendar.YEAR);

		//no of labels
		//if endMonth is following year 
		int size = (endMonth - startMonth < 0) ? endMonth - startMonth  + 1: (endMonth + 12 - startMonth) + 1;
		
		List<String> labels = new ArrayList<String>();

		int month = startMonth;
		int year = startYear;
		
		String label = "";
		//iterate every month from start month till it hits the end month
		while(month != endMonth || year != endYear) {
			
			label = getMonthInString(month) + "-" + year;
			labels.add(label);
			
			//if month in question is not december, just increment month
			if(month != 12) {
				month++;
				
			}
			
			//if month in question is decemnber, reset to jan , and increment year
			else {
				month = 1;
				year++;
			}
			
		
		}
		
		label = getMonthInString(month) + "-" + year;
		labels.add(label);
		
		return labels;
				
	}

}
