package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import view.LobbyScene;
import view.LoginScene;
import view.SignupScene;
import view.SinglePlayerLobbyScene;
import view.WindowManager;

import java.util.regex.Pattern;
/**
 * Database is used to make a connection to the PostGreSQL database to
 * handle the registration and login by getting the information from the
 * database. Checks are involved to check if the inputs are valid or not.
 * @author Heh Shyang Lee(Rain)
 *
 */
public class Database {
	
	private String first;
	private String second;
	private String email;
	private String password;
	private String confirmPass;
	/**
	 * The getConnection() method is used to get the connection to the database.
	 * @return a connection to the database
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		
		try {  
			
			String url = "jdbc:postgresql://25.65.231.255:5432/Lost&Found";
			String username = "postgres";
			String password = "postgres";
			
			Connection conn = DriverManager.getConnection(url,username,password);
						
			return conn;
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	/**
	 * The checkEmail() method is used to check if the email exist in the database.
	 * @param email takes the email input on the login's email text box
	 * @return boolean true if email exists, false if it does not
	 */
	private boolean checkEmail(String email) {
		
		this.email = email;
		
		boolean checker = false;
		
		try {
			
			PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM player WHERE email = ?");
			
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				checker = true;
			}
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return checker;
		
	}
	/**
	 * The checkFormat() method is used to check if the email is in a valid form or not.
	 * @param email takes the email input on the registration's email text box.
	 * @return boolean true if email is valid, false if it is not
	 */
	private boolean checkFormat(String email) {
		
	String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
               "[a-zA-Z0-9_+&*-]+)*@" + 
               "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
               "A-Z]{2,7}$"; 
                  
	Pattern pat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE); 
	if (email == null) 
			return false; 
	
	return pat.matcher(email).matches(); 
	
	}
	
	private boolean checkCase(String email) {
		
		for (int i = 0; i < email.length(); i++) {
			if (Character.isUpperCase(email.charAt(i))) {
				return true;
			} 
		}
		
		return false;
		
	}
	/**
	 * The createAccount() method is used to create an account and store the details in the database
	 * if all information input by the user are valid.
	 * @param first takes the first name of the user
	 * @param second takes the second name of the user
	 * @param email takes the email of the user
	 * @param password takes the password of the user
	 * @param confirmPass takes the retyped password to check if it matches the password
	 * @return boolean true if user's account is created successfully, false if not
	 */
	public boolean createAccount(String first, String second, String email, String password, String confirmPass) {

		boolean check = false;
		
		this.first = first;
		this.second = second;
		this.email = email;
		this.password = password;
		this.confirmPass = confirmPass;
		
		if (Character.isLowerCase(first.charAt(0)) || Character.isLowerCase(second.charAt(0))) {
			first = first.substring(0,1).toUpperCase() + first.substring(1).toLowerCase();
			second = second.substring(0,1).toUpperCase() + second.substring(1).toLowerCase();
		}
		if (first.equals("") || second.equals("")) {
			SignupScene.signupErrorPane.setVisible(true);
		} 
		else if (email.equals("")) {
			SignupScene.signupErrorPane.setVisible(true);
		}
		else if (checkEmail(email)) {
			SignupScene.signupErrorPane.setVisible(true);
		}
		else if (!checkFormat(email)) {
			SignupScene.signupErrorPane.setVisible(true);
		}
		else if (checkCase(email)) {
			SignupScene.signupErrorPane.setVisible(true);
		}
		else if (password.equals("") || password.length() < 8) {
			SignupScene.signupErrorPane.setVisible(true);
		}
		else if (!confirmPass.equals(password)) {
			SignupScene.signupErrorPane.setVisible(true);
		} 
		else if (!checkEmail(email)) {
			
			try {
		
				PreparedStatement ps = getConnection().prepareStatement("INSERT INTO player (firstname, lastname, email, password) VALUES (?,?,?,?)");
			
				ps.setString(1, first);
				ps.setString(2, second);
				ps.setString(3, email);
				ps.setString(4, password);
			
				if (ps.executeUpdate() > 0) {
					SignupScene.signupSuccessPane.setVisible(true);
					check = true;
				}
			
			} catch (SQLException e) {
				System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		return check;
		
		
	}
	/**
	 * The login() method is used to log the user into the main menu if the email and password
	 * match the both in the database.
	 * @param email takes the email of the user used for registration
	 * @param password takes the password of the user used for registration
	 * @return String the correct email entered by the user
	 */
	public String login(String email, String password) {
		
		String returnEmail = "";
		
		this.email = email;
		this.password = password;
		
		try {
			
			PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM player WHERE email = ? AND password = ?");
			
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				//info(null, null, "WELCOME BACK! :D");
				returnEmail = email;
			}
			else {
				LoginScene.loginErrorPane.setVisible(true);
			}
			
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnEmail;
		
	}
	
}
