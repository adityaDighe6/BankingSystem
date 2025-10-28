package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Users {
	//Create variables of type class Connection and Scanner.
	private Connection con;
	private Scanner sc;
	//End
	public Users(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	//Method to register a new user into system.
	public void regsiter() {
		System.out.println("Enter the Full Name : ");
		String full_name = sc.next();
		
		System.out.println("Enter the email : ");
		String email = sc.next();
		
		System.out.println("Enter the password : ");
		String password = sc.next();
		
		if(user_exists(email)) {
			System.out.println("User with this email already exists.");
		}
		
		String register_query = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)"; 
		
		try {
			PreparedStatement preparedStatement = con.prepareStatement(register_query);
			preparedStatement.setString(1, full_name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			
			int noOfRowsAffected = preparedStatement.executeUpdate();
			
			if(noOfRowsAffected > 0) {
				System.out.println("Registration successful.");
			}
			else {
				System.out.println("Registration failed.");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	//Method for login
	public String login() {
		
		System.out.println("Enter the email : ");
		String email = sc.next();
		
		System.out.println("Enter the password : ");
		String password = sc.next();
		
		String login_query = "SELECT * FROM users WHERE email = ? AND password = ?";
		
		try {
			PreparedStatement preStatement = con.prepareStatement(login_query);
			preStatement.setString(1, email);
			preStatement.setString(2, password);
			
			ResultSet rs = preStatement.executeQuery();
			
			if(rs.next()) {
				return email;
			}
			else {
				return null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//End
	//Method to check whether user exists or not. Return true if exists.
	public boolean user_exists(String email) {
		/*
		 * primary key of the users table is email. Hence results will be on the basis of email.
		 */
		String query = "SELECT * FROM users WHERE email = ?";
		
		try {
			PreparedStatement preparedStatemet = con.prepareStatement(query);
			preparedStatemet.setString(1, email);
			
			ResultSet rs = preparedStatemet.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
			
		} catch (SQLException e) {
			
		}
		return false;
	}
	//End
}
