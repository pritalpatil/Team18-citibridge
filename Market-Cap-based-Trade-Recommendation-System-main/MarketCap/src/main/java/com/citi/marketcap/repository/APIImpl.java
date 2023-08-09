package com.citi.marketcap.repository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import com.citi.marketcap.dto.Stock;
import com.citi.marketcap.dto.Stock1;
import com.citi.marketcap.dto.Stock2;

@Repository
public class APIImpl implements API
{
	public static ArrayList<Stock> topFive = new ArrayList<>();
	public static ArrayList<Stock1> stockSymbolsAL = new ArrayList<>();

	@Override
	public void recommend(String str)
	{
		ArrayList<String> tickerList = new ArrayList<>();

		String APIkey = "6d9bce770b5f2123cc374e05f79ec341";// "f6baf1e3ee826845e485e907450fddbe";

		// small stocks have market cap between 300 million to 2 billion dollars
		String small = "https://financialmodelingprep.com/api/v3/stock-screener?marketCapLowerThan=159862800000&marketCapMoreThan=23981595000&exchange=NASDAQ&apikey="
				+ APIkey;

		// mid stocks have market cap between 2-10 billion dollars
		String medium = "https://financialmodelingprep.com/api/v3/stock-screener?marketCapLowerThan=797083000000&marketCapMoreThan=159862800000&exchange=NASDAQ&apikey="
				+ APIkey;

		// large stocks have market cap >5 billion dollars
		String large = "https://financialmodelingprep.com/api/v3/stock-screener?marketCapMoreThan=398541500000&exchange=NASDAQ&apikey="
				+ APIkey;

		URL marketCap = null;

		StringBuilder informationString = new StringBuilder();

		try
		{
			if (str.compareTo("small") == 0) marketCap = new URL(small);
			else if (str.compareTo("mid") == 0) marketCap = new URL(medium);
			else if (str.compareTo("large") == 0) marketCap = new URL(large);

			HttpURLConnection conn = (HttpURLConnection) marketCap.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			// Check if connect is made
			int responseCode = conn.getResponseCode();

			// 200 OK
			if (responseCode != 200)
			{
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}
			else
			{
				Scanner scanner = new Scanner(marketCap.openStream());

				while (scanner.hasNext())
				{
					informationString.append(scanner.nextLine());
				}
				// Close the scanner
				scanner.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println(informationString);

		tickerList.clear();
		topFive.clear();
		stockSymbolsAL.clear();

		StringBuilder stockSymbolsSB = new StringBuilder();
		JSONParser parse = new JSONParser();
		JSONArray dataObject;
		try
		{
			dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

			for (int i = 0; i < dataObject.size(); i++)
			{
				JSONObject countryData = (JSONObject) dataObject.get(i);

				stockSymbolsSB.append(countryData.get("symbol").toString());
				if (i != dataObject.size() - 1) stockSymbolsSB.append(",");

				Stock1 s1 = new Stock1(countryData.get("symbol").toString(),
						Double.parseDouble(countryData.get("price").toString()),
						Double.parseDouble(countryData.get("beta").toString()),
						Double.parseDouble(countryData.get("marketCap").toString()),
						Double.parseDouble(countryData.get("volume").toString()));

				stockSymbolsAL.add(s1);
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		StringBuffer temp = new StringBuffer();
		try
		{
			marketCap = new URL(
					"https://query1.finance.yahoo.com/v7/finance/quote?symbols=" + stockSymbolsSB.toString());

			HttpURLConnection conn = (HttpURLConnection) marketCap.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			// Check if connect is made
			int responseCode = conn.getResponseCode();

			// 200 OK
			if (responseCode != 200)
			{
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}
			else
			{
				Scanner scanner = new Scanner(marketCap.openStream());

				while (scanner.hasNext())
				{
					temp.append(scanner.nextLine());
				}
				// Close the scanner
				scanner.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayList<Stock2> yahooStocksAL = new ArrayList<Stock2>();

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		JSONObject json1 = null;
		JSONArray dataObject1;

		try
		{
			json = (JSONObject) parser.parse(temp.toString());
			String s1 = json.get("quoteResponse").toString();

			json1 = (JSONObject) parser.parse(s1);
			String s2 = json1.get("result").toString();

			dataObject1 = (JSONArray) parser.parse(s2);

			for (int i = 0; i < dataObject1.size(); i++)
			{
				JSONObject countryData = (JSONObject) dataObject1.get(i);

				double changePercent = 0;
				String rating = "null";
				double pe = 0;
				try
				{
					changePercent = Double.parseDouble(countryData.get("twoHundredDayAverageChangePercent").toString());
					rating = countryData.get("averageAnalystRating").toString();
					pe = Double.parseDouble(countryData.get("trailingPE").toString());
				}
				catch (Exception e)
				{

				}

				Stock2 stock2 = new Stock2(countryData.get("symbol").toString(), rating, changePercent, pe);

				yahooStocksAL.add(stock2);
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		HashMap<String, Stock2> hmp = new HashMap<>();

		for (int i = 0; i < stockSymbolsAL.size(); i++)
		{
			hmp.put(stockSymbolsAL.get(i).getSymbol(), new Stock2());
		}

		for (int i = 0; i < yahooStocksAL.size(); i++)
		{
			if (hmp.containsKey(yahooStocksAL.get(i).getSymbol()))
			{
				hmp.put(yahooStocksAL.get(i).getSymbol(), yahooStocksAL.get(i));
			}
		}

		topFive = stockRecommendation(hmp);
	}

	public ArrayList<Stock> stockRecommendation(HashMap<String, Stock2> hmp)
	{
		ArrayList<Stock> topPerformance = new ArrayList<Stock>();
		ArrayList<Stock> topRating = new ArrayList<Stock>();
		ArrayList<Stock> topFiveFinal = new ArrayList<Stock>();

		// convert HashMap into List
		List<Entry<String, Stock2>> list = new LinkedList<Entry<String, Stock2>>(hmp.entrySet());

		sortHashMap(list); // sort hmp based on - twoHundredDayAverageChangePercent

		Map<String, Stock2> sortedMap = new LinkedHashMap<String, Stock2>();
		for (Entry<String, Stock2> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		for (Entry<String, Stock2> entry : sortedMap.entrySet())
		{
			double mc = 0.0;
			double beta = 0.0;
			double vol = 0.0;
			double p = 0.0;

			for (int i = 0; i < stockSymbolsAL.size(); i++)
			{
				if (stockSymbolsAL.get(i).getSymbol().compareTo(entry.getKey()) == 0)
				{
					mc = stockSymbolsAL.get(i).getMarketCap();
					beta = stockSymbolsAL.get(i).getBeta();
					vol = stockSymbolsAL.get(i).getVolume();
					p = stockSymbolsAL.get(i).getPrice();
					break;
				}
			}

			Stock s = new Stock(entry.getKey().toString(), p, beta, mc, vol, entry.getValue().getAverageAnalystRating(),
					entry.getValue().getTwoHundredDayAverageChangePercent(), entry.getValue().getTrailingPE(), "", "",
					0);

			topPerformance.add(s);
		}

		// check for the average analyst rating
		if (topPerformance.size() > 5)
		{
			for (int i = 0; i < topPerformance.size(); i++)
			{
				if ((topPerformance.get(i).getAverageAnalystRating() != null))
				{
					if (topPerformance.get(i).getAverageAnalystRating().contains("Buy"))
					{
						topRating.add(topPerformance.get(i));
					}
				}
			}
		}

		if (topRating.size() > 5)
		{
			for (int i = 0; i < topRating.size(); i++)
			{
				if ((topRating.get(i).getTrailingPE() >= 15) && (topRating.get(i).getTrailingPE() <= 20))
				{
					topFiveFinal.add(topRating.get(i));
				}
			}
		}

		ArrayList<Stock> ans = new ArrayList<Stock>();
		if (topFiveFinal.size() >= 5)
		{
			ans.add(topFiveFinal.get(0));
			ans.add(topFiveFinal.get(1));
			ans.add(topFiveFinal.get(2));
			ans.add(topFiveFinal.get(3));
			ans.add(topFiveFinal.get(4));
			return ans;
		}
		else if (topRating.size() >= 5)
		{
			ans.add(topRating.get(0));
			ans.add(topRating.get(1));
			ans.add(topRating.get(2));
			ans.add(topRating.get(3));
			ans.add(topRating.get(4));
			return ans;
		}
		else
		{
			ans.add(topPerformance.get(0));
			ans.add(topPerformance.get(1));
			ans.add(topPerformance.get(2));
			ans.add(topPerformance.get(3));
			ans.add(topPerformance.get(4));
			return ans;
		}
	}

	public List<Entry<String, Stock2>> sortHashMap(List<Entry<String, Stock2>> list)
	{
		// sorting the list elements
		Collections.sort(list, new Comparator<Entry<String, Stock2>>()
		{
			@Override
			public int compare(Entry<String, Stock2> o1, Entry<String, Stock2> o2)
			{
				// sort in descending order
				if (o1.getValue().getTwoHundredDayAverageChangePercent() == o2.getValue()
						.getTwoHundredDayAverageChangePercent())
				{
					return 0;
				}
				else if (o1.getValue().getTwoHundredDayAverageChangePercent() > o2.getValue()
						.getTwoHundredDayAverageChangePercent())
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
		});

		return list;
	}
}
