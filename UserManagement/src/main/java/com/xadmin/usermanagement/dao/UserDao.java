package com.xadmin.usermanagement.dao;

import com.xadmin.usermanagement.bean.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//import org.apache.catalina.User;

public class UserDao {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Madhu#123";
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, country) values " + " (?, ?, ?);";
	
	private static final String SELECT_USER_BY_ID = "select id, name, email, country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	public UserDao() {
	}
	
	protected Connection getConnection()
	{
		Connection connection = null;
		
		try
		{
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return connection;
	}
	
	
	//insert user method
	
	public void insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		
		try(Connection connection = getConnection(); 
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL))
		{
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3,  user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			printSQLException(e);
		}
	}

	
	//select user by id
	
	public User selectUser(int id)
	{
		User user = null;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);)
		{
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	//Select all users
	
	public List<User> selectAllUsers()
	{
		List<User> users = new ArrayList<>();
		
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS))
		{
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
	//update user
	
	public boolean UpdateUSer(User user) throws SQLException
	{
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);)
		{
			System.out.println("updated User:"+ preparedStatement);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.setInt(4, user.getId());
			
			rowUpdated = preparedStatement.executeUpdate() > 0;
			
		} 
		return rowUpdated;
	}
	
	//delete User
	
	public boolean DeleteUser(int id) throws SQLException
	{
		boolean rowDeleted;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);){
				preparedStatement.setInt(1, id);
				rowDeleted = preparedStatement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
