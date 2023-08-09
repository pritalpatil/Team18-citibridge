package com.citi.marketcap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Stock2 {
	String symbol;
	String averageAnalystRating;
	double twoHundredDayAverageChangePercent;
	double trailingPE;
}