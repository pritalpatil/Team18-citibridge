package com.citi.marketcap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.citi.marketcap.controller.LoginController;
import com.citi.marketcap.dto.Stock;

@Repository
public class StockRepositoryImpl implements StockRepository
{
	@Autowired
	DataSource dataSource;

	@Override
	public String saveStock(String str, int numberOnly)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Stock stock = null;
		for (int i = 0; i < APIImpl.topFive.size(); i++)
		{
			if (APIImpl.topFive.get(i).getSymbol().compareTo(str) == 0)
			{
				// get current date
				String m, date;
				int am_pm;
				int d, y;
				String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
						"Dec" };

				GregorianCalendar gcalendar = new GregorianCalendar();
				d = gcalendar.get(Calendar.DATE);
				m = months[gcalendar.get(Calendar.MONTH)];
				y = gcalendar.get(Calendar.YEAR);
				date = Integer.toString(d) + " " + m + " " + Integer.toString(y);

				// get current time
				int h, min, sec;
				String time;
				h = gcalendar.get(Calendar.HOUR);
				min = gcalendar.get(Calendar.MINUTE);
				sec = gcalendar.get(Calendar.SECOND);
				am_pm = gcalendar.get(Calendar.AM_PM);
				
				time = Integer.toString(h) + ":" + Integer.toString(min) + ":" + Integer.toString(sec);
				
				if(am_pm == 0)
					time = time + " AM";
				else
					time = time + " PM";

				stock = APIImpl.topFive.get(i);
				APIImpl.topFive.get(i).setDate(date);
				APIImpl.topFive.get(i).setTime(time);

				try
				{
					connection = dataSource.getConnection();
					String query = "insert into user_saved_stock values(?,?,?,?,?,?)";
					preparedStatement = connection.prepareStatement(query);

					preparedStatement.setInt(1, LoginController.user.getUserId());
					preparedStatement.setString(2, stock.getSymbol());
					preparedStatement.setDouble(3, stock.getPrice());
					preparedStatement.setInt(4, numberOnly);
					preparedStatement.setString(5, stock.getDate());
					preparedStatement.setString(6, stock.getTime());

					int res = preparedStatement.executeUpdate();

					if (res > 0) 
						return "success";
					else
						return "failure";
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				break;
			}
		}
		return "failure";
	}

	@Override
	public ArrayList<Stock> getSaved()
	{
		ArrayList<Stock> list = null;
		Connection connection = null;
		ResultSet resultSet = null;
		String query = "select * from user_saved_stock";
		boolean flag = false;
		try
		{
			connection = dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Stock s = new Stock();

				if (LoginController.user.getUserId() == resultSet.getInt(1))
				{
					s.setSymbol(resultSet.getString(2));
					s.setPrice(resultSet.getDouble(3));
					s.setQuantity(resultSet.getInt(4));
					s.setDate(resultSet.getString(5));
					s.setTime(resultSet.getString(6));
					if (s != null && !flag)
					{ 
						flag = true;
						list = new ArrayList<>();
					}
					list.add(s);
				}
			}
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
		}
		return null;
	}

	@Override
	public void unsaveStock(ArrayList<Stock> savedStocks, String stock)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Stock s1 = null;

		String[] arrOfStr = stock.split(" ");

		for (int i = 0; i < savedStocks.size(); i++)
		{
			if (savedStocks.get(i).getSymbol().equals(arrOfStr[0]))
			{
				s1 = savedStocks.get(i);

				savedStocks.get(i).setTime(arrOfStr[1] + " " + arrOfStr[2]);

				try
				{
					connection = dataSource.getConnection();
					String query = "delete from user_saved_stock where (userId=? and symbol=? and time=?)";
					preparedStatement = connection.prepareStatement(query);

					preparedStatement.setInt(1, LoginController.user.getUserId());
					preparedStatement.setString(2, s1.getSymbol());
					preparedStatement.setString(3, s1.getTime());

					System.out.println(preparedStatement.toString());

					int res = preparedStatement.executeUpdate();

					if (res > 0) 
						System.out.println("deleted");
					else
						System.out.println("not deleted");
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}

				break;
			}
		}
	}
}
