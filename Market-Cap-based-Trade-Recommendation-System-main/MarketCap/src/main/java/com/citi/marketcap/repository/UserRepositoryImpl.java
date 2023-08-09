package com.citi.marketcap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.citi.marketcap.controller.LoginController;
import com.citi.marketcap.dto.User;

@Repository
public class UserRepositoryImpl implements UserRepository
{
	@Autowired
	DataSource dataSource;

	@Override
	public String loggedIn(User user)
	{

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			connection = dataSource.getConnection();
			String query = "select * from user where userName=?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getUserName());
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				if (user.getPassword().equals(resultSet.getString(3)))
				{
					return String.valueOf(resultSet.getInt(1));
				}
				else
				{
					return "Incorrect Password!! Please try again";
				}
			}
			else
			{
				return "User not found!! Please enter correct UserId";
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return "fail";
	}

	@Override
	public void logOut(User user)
	{
		LoginController.user = null;
	}
}
