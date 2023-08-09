package com.citi.marketcap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Stock {
	String symbol;
	double price;
	double beta;
	double marketCap;
	double volume;
	String averageAnalystRating;
	double twoHundredDayAverageChangePercent;
	double trailingPE;
	String date;
	String time;
	int quantity;
	
	public String toString() {
		
		return symbol;
	}
}