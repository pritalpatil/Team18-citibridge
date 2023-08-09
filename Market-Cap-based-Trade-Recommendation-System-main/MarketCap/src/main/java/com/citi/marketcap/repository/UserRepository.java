package com.citi.marketcap.repository;

import com.citi.marketcap.dto.User;

public interface UserRepository
{
	public String loggedIn(User user);
	
	public void logOut(User user);
}
