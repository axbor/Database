package dbtLab3;

import java.sql.*;
import java.util.LinkedList;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;
	private String userNameQuery;
	private String movieQuery;
	private String dateQuery;
	private String performanceQuery;
	private String bookQuery;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
		  userNameQuery = "SELECT name FROM Users WHERE userName = ?";
		  moviesQuery = "SELECT name FROM Movies";
		  dateQuery = "SELECT showDate FROM Performances WHERE movieName = ?";
		  performanceQuery = "SELECT freeSeats, theaterName FROM Performances WHERE movieName = ? and showDate = ?";
		  bookQuery = "insert into Tickets ";
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/**
	 * Check if the user exists in the database
	 * 
	 * @param userId
	 * @return true if userId exists in database
	 */
	public boolean checkUser(String userId) {
		return false;
	}

	/**
	 * 
	 * @return list of all movies in database
	 */
	public LinkedList<String> getmovies() {
	
	ResultSet sqlMovies;
	PreparedStatement getMovies;
	
	LinkedList<String> dates = new LinkedList<String>();

	try{
		getMovies = conn.prepareStatement(movieQuery);
		sqlMovies = getMovies.executeQuery();

		while(sqlMovies.next()){
			dates.add(sqlMovies.getString("name"));
		}

	} catch(SQLException e){
		e.printStackTrace();
	}
			
	return dates;
	
	}

	/**
	 * 
	 * @param movieName
	 * @return returns a list of all dates for movie movieName
	 */
	public LinkedList<String> getDates(String movie){
		ResultSet sqlDates;
		PreparedStatement getDates;
		
		LinkedList<String> dates = new LinkedList<String>();

		try{
			getDates = conn.prepareStatement(dateQuery);
			getDates.setString(1, movie);
			sqlDates = getDates.executeQuery();

			while(sqlDates.next()){
				dates.add(sqlDates.getString("showDate"));
			}
	
		} catch(SQLException e){
			e.printStackTrace();
		}
				
		return dates;
		
	}

	public Performance getPerformance(String movieName, String date) {
		
		
		return null;
	}

	/**
	 * Books a ticket for the performance
	 * @param performance
	 */
	public void bookTicket(String movieName,String date) {
		// TODO Auto-generated method stub
		
	}

	public LinkedList<String> getMovieDates(String movieName) {
		// TODO Auto-generated method stub
		return null;
	}

}
