package com.citi.marketcap.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.citi.marketcap.dto.Stock;
import com.citi.marketcap.repository.APIImpl;
import com.citi.marketcap.service.APIService;
import com.citi.marketcap.service.StockService;
import com.citi.marketcap.service.UserService;

@Controller
public class StockController
{
	@Autowired
	StockService stockService;
	
	@Autowired
	UserService userService;

	@Autowired
	APIService apiService;

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcomePage()
	{
		return "welcome";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.POST, params = { "type" })
	public void display(ModelMap modelMap, @RequestParam(value = "type") String str)
	{
		apiService.recommend(str);
		modelMap.clear();
		modelMap.put("response", APIImpl.topFive);
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.POST, params = { "ticker" })
	public void save(ModelMap modelMap, @RequestParam(value = "ticker") String str, @RequestParam(value = "quantity") String quantity)
	{
		int numberOnly= Integer.valueOf(quantity.replaceAll("[^0-9]", ""));
		System.out.println(numberOnly);
		modelMap.put("response", APIImpl.topFive);
		stockService.saveStock(str, numberOnly);
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.POST, params = { "show" })
	public void show(ModelMap modelMap, @RequestParam(value = "show") String str)
	{
		ArrayList<Stock> show = stockService.getSaved();
		modelMap.put("showall", show);
		if(show == null)
			modelMap.put("notsaved", "You have not yet saved any stocks");
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.POST, params = { "unsave" })
	public void unsave(ModelMap modelMap, @RequestParam(value = "unsave") String str)
	{
		ArrayList<Stock> savedStocks = stockService.getSaved();
		stockService.unsaveStock(savedStocks, str);
		savedStocks = stockService.getSaved();
		modelMap.put("showall", savedStocks);
	}
	
	@RequestMapping(value = "/welcome", method = RequestMethod.POST, params = { "logout" })
	public String logout(ModelMap modelMap, @RequestParam(value = "logout") String str)
	{
		userService.logOut(LoginController.user);
		return "redirect:/login";
	}
}
