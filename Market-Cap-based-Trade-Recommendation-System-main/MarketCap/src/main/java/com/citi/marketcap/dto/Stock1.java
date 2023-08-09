package com.citi.marketcap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Stock1
{
	String symbol;
	double price;
	double beta;
	double marketCap;
	double volume;
}