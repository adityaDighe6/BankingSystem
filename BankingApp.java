package bankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

//Driver class for the project.
public class BankingApp {
	
	//Step 2 : Add url, username and password.
	private static final String url = "jdbc:mysql://localhost:3306/banking_db";
	private static final String user = "root";
	private static final String password = "aditya";
	//End
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		//Step 1 : Load the driver.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Drivers loaded successfully...");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		//End
		
		//Step 3 : Establish a connection...
		try {
			Connection con = DriverManager.getConnection(url, user, password);
			System.out.println("Connection established successfully...");
			
			Scanner sc = new Scanner(System.in);
			
			AccountManager accmanager = new AccountManager(con,sc);
			Users users = new Users(con,sc);
			Accounts accounts = new Accounts(con,sc);
			
			String email;
			long account_number;
			
			while(true) {
				System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = sc.nextInt();
                
                switch(choice1) {
                	case 1 : users.regsiter();
                			 break;
                			 
                	case 2 : email = users.login();
                			 if(email != null) {
                				 System.out.println("User already logged in.");
                			 
                			 if(!accounts.account_exists(email)) {
                				 System.out.println("1. Open a new Bank Account");
                                 System.out.println("2. Exit");
                                 
                                 if(sc.nextInt() == 1) {
                                     account_number = accounts.open_account(email);
                                     System.out.println("Account Created Successfully");
                                     System.out.println("Your Account Number is: " + account_number);
                                 }
                			 }
                			 else {
                				 break;
                			 }
                
                			 }	 
                			
                	account_number = accounts.getAccount_number(email);
                	 int choice2 = 0;
                     while (choice2 != 5) {
                         System.out.println();
                         System.out.println("1. Debit Money");
                         System.out.println("2. Credit Money");
                         System.out.println("3. Transfer Money");
                         System.out.println("4. Check Balance");
                         System.out.println("5. Log Out");
                         System.out.println("Enter your choice: ");
                         choice2 = sc.nextInt();
                         
                         switch (choice2) {
                         case 1:
                             accmanager.debit_money(account_number);
                             break;
                         case 2:
                        	 accmanager.credit_money(account_number);
                             break;
                         case 3:
                        	 accmanager.transfer_money(account_number);
                             break;
                         case 4:
                        	 accmanager.getBalance(account_number);
                             break;
                         case 5:
                             break;
                         default:
                             System.out.println("Enter Valid Choice!");
                             break;
                     }
                 }
   	
                	case 3 : System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                    		 System.out.println("Exiting System!");
                    		 return;
                    		 
                    default : System.out.println("Enter Valid Choice");
                    		  break;		  
                }
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		//End
	}

}
