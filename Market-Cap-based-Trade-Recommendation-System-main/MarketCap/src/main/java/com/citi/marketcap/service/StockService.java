package com.citi.marketcap.service;

import java.util.ArrayList;

import com.citi.marketcap.dto.Stock;

public interface StockService
{
	public String saveStock(String str, int numberOnly);
	
	public ArrayList<Stock> getSaved();
	
	public void unsaveStock(ArrayList<Stock> savedStocks,String stock);
}