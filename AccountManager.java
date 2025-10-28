package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	
	private Connection con;
	private Scanner sc;
	
	public AccountManager(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}
	//Method to get the balance.
	public void getBalance(long account_number) {
		
		System.out.println("Enter Security pin : ");
		String security_pin = sc.next();
		
		try {
			PreparedStatement preparedStatement = con.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
				Double balance = rs.getDouble("balance");
				System.out.println("Balance :"+balance);
			}
			else {
				System.out.println("Inavlid Pin");
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	//End
	//method for crediting money
	public void credit_money(long account_number) throws SQLException {
		System.out.println("Enter the amount : ");
		double amount = sc.nextDouble();
		
		System.out.println("Enter the security pin :");
		String security_pin = sc.next();
		
		try {
			con.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				
				ResultSet rs = preparedStatement.executeQuery();
				
				if(rs.next()) {
					String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
					
					PreparedStatement preparedStatement1 = con.prepareStatement(credit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					
					int noOfRowsAffected = preparedStatement1.executeUpdate();
					
					if(noOfRowsAffected > 0) {
						System.out.println("RS."+amount+"Credited successfully.");
						con.commit();
						con.setAutoCommit(true);
					}
				}
				else {
					System.out.println("Transaction failed...");
					con.rollback();
					con.setAutoCommit(true);
				}
			}
			else {
				System.out.println("Invalid security pin...");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	//method for debiting money
	public void debit_money(long account_number) throws SQLException {
		System.out.println("Enter the amount : ");
		double amount = sc.nextDouble();
		
		System.out.println("Enter the security pin : ");
		String security_pin = sc.next();
		
		try {
			con.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				
				ResultSet rs = preparedStatement.executeQuery();
				
				if(rs.next()) {
					String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
					
					PreparedStatement preparedStatement1 = con.prepareStatement(debit_query);
					
					preparedStatement1.setDouble(1, account_number);
					preparedStatement1.setString(2, security_pin);
					
					int noOfRowsAffected = preparedStatement1.executeUpdate();
					if(noOfRowsAffected > 0) {
						System.out.println("Rs."+amount+"debited sucessfully...");
						con.commit();
						con.setAutoCommit(true);
					}
					else {
						System.out.println("Transaction failed...");
						con.rollback();
						con.setAutoCommit(true);
					}
				}
				else {
					System.out.println("Invalid security pin...");
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//End
	//Method for transferring money
	public void transfer_money(long sender_account_number) throws SQLException {
		System.out.println("Enter the receiver's account number : ");
		long receiver_account_number = sc.nextLong();
		
		System.out.println("Enter the amount : ");
		double amount = sc.nextDouble();
		
		System.out.println("Enter the security pin : ");
		String security_pin = sc.next();
		
		
		try {
			con.setAutoCommit(false);
			if(sender_account_number != 0 && receiver_account_number != 0) {
				PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				
				ResultSet rs = preparedStatement.executeQuery();
				
				if(rs.next()) {
					
					double current_balance = rs.getDouble("balance");
					
					if(amount<=current_balance) {
						String credit_query = "UPDATE accounts SET balanace = balance + ? WHERE account_number = ?";
						String dedit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						
						PreparedStatement preparedStatementDebit = con.prepareStatement(credit_query);
						PreparedStatement preparedStatementCredit = con.prepareStatement(dedit_query);
						
						
						preparedStatementCredit.setLong(1, receiver_account_number);
						preparedStatementCredit.setDouble(2, amount);
						preparedStatementDebit.setLong(1, sender_account_number);
						preparedStatementDebit.setDouble(2, amount);
						
						int noOfRowsAffected1 = preparedStatementCredit.executeUpdate();
						int noOfRowsAffetec2 = preparedStatementDebit.executeUpdate();
						
						if(noOfRowsAffected1 >0 && noOfRowsAffetec2 > 0) {
							System.out.println("Transaction successful..");
							System.out.println("Rs."+amount+"Transfer successful..");
							con.commit();
							con.setAutoCommit(true);
						}
						else {
							System.out.println("Transaction failed...");
							con.rollback();
							con.setAutoCommit(true);
						}
					}
					else {
						System.out.println("Insufficient balance.");
					}
				}
				else {
					System.out.println("Invalid security pin...");
				}
			}
		}	
		catch(SQLException e) {
			
		}
	}
	//End
}
