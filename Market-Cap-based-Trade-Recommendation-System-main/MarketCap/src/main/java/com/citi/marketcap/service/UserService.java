package com.citi.marketcap.service;

import com.citi.marketcap.dto.User;

public interface UserService
{
	public String loggedIn(User user);
	
	public void logOut(User user);
}
