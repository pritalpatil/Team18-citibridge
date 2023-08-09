package com.citi.marketcap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.marketcap.repository.API;

@Service("apiService")
public class APIServiceImpl implements APIService
{
	@Autowired
	API api;
	
	@Override
	public void recommend(String str)
	{
		api.recommend(str);
	}
}
