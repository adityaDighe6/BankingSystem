package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	
	private Connection con;
	private Scanner sc;
	
	public Accounts(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	//method for opening a new account.
	public long open_account(String email) {
		
		if(!account_exists(email)) {
			String open_acc_query = "INSERT INTO accounts (account_number, full_name, email, balance, security_pin) VALUES (? , ? , ? , ? , ?)";
			
			System.out.println("Enter the full_name : ");
			String full_name = sc.next();
			
			System.out.println("Enter the balance : ");
			double balance = sc.nextDouble();
			
			System.out.println("Enter the security pin : ");
			String security_pin = sc.next();
			
			try {
				long account_number = generateAccountNumber();
				PreparedStatement preparedStatement = con.prepareStatement(open_acc_query);
				
				preparedStatement.setLong(1,account_number);
				preparedStatement.setString(2,full_name);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4,balance);
				preparedStatement.setString(5,security_pin);
				
				int noOfRowsAffected = preparedStatement.executeUpdate();
				if(noOfRowsAffected > 0) {
					return account_number;
				}
				else {
					throw new RuntimeException("Account creation failed.");
				}
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account already exists.");
	}
	//End
	//method for retrieving account number
	public long getAccount_number(String email) {
		
		String query = "SELECT account_number FROM accounts WHERE email = ?";
		
		try {
			PreparedStatement preparedstatement = con.prepareStatement(query);
			preparedstatement.setString(1, email);
			
			ResultSet rs = preparedstatement.executeQuery();
			
			if(rs.next()) {
				return rs.getLong("account_number");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account Number dosen't exist.");
	}
	//End
	//Method for generating account number.
	public long generateAccountNumber() {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
			
			if(rs.next()) {
				long last_account_number = rs.getLong("account_number");
				return last_account_number+1;
			}
			else {
				return 10000100;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 10000100;
	}
	//End
	//Method for checking whether account exists or not.
	public boolean account_exists(String email) {
		
		String query = "SELECT account_number FROM accounts WHERE email = ?";
		
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//End
}
