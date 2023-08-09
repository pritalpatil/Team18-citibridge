package com.citi.marketcap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.citi.marketcap.dto.User;
import com.citi.marketcap.service.UserService;

@Controller
public class LoginController
{

	public static User user = null;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage()
	{
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String welcomePage(ModelMap modelMap, @RequestParam String userName, @RequestParam String password)
	{
		user = new User(0, userName, password);
		String isLoggedIn = userService.loggedIn(user);

		// check if string is an integer
		if (isLoggedIn.matches("\\d+"))
		{
			user.setUserId(Integer.valueOf(isLoggedIn));
			return "redirect:/welcome";
		}

		modelMap.put("error", isLoggedIn);
		return "login";
	}
}
