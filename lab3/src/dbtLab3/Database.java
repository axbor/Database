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
	private String freeSeatsQuery;
	private String setFreeSeatsQuery;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
		userNameQuery = "SELECT name FROM Users WHERE userName = ?";
		movieQuery = "SELECT name FROM Movies";
		dateQuery = "SELECT showDate FROM Performances WHERE movieName = ?";
		performanceQuery = "SELECT freeSeats, theaterName FROM Performances WHERE movieName = ? and showDate = ?";
		bookQuery = "INSERT INTO Tickets values(null, ?, ?, ?)";
		freeSeatsQuery = "SELECT freeSeats FROM performances WHERE movieName = ? and showDate = ?";
		setFreeSeatsQuery = "UPDATE Performances set freeSeats = ? WHERE movieName = ? and showDate = ?";

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
	public String getUser(String userId) {
		PreparedStatement getUser;
		ResultSet sqlUser;
		try {
			getUser = conn.prepareStatement(userNameQuery);
			getUser.setString(1, userId);
			sqlUser = getUser.executeQuery();
			while (sqlUser.next()) {
				String userName = sqlUser.getString("name");
				if (userName != null) {
					return userName;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @return list of all movies in database
	 */
	public LinkedList<String> getmovies() {

		ResultSet sqlMovies;
		PreparedStatement getMovies;

		LinkedList<String> dates = new LinkedList<String>();

		try {
			getMovies = conn.prepareStatement(movieQuery);
			sqlMovies = getMovies.executeQuery();

			while (sqlMovies.next()) {
				dates.add(sqlMovies.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dates;

	}

	/**
	 * 
	 * @param movieName
	 * @return returns a list of all dates for movie movieName
	 */
	public LinkedList<String> getDates(String movie) {
		ResultSet sqlDates;
		PreparedStatement getDates;

		LinkedList<String> dates = new LinkedList<String>();

		try {
			getDates = conn.prepareStatement(dateQuery);
			getDates.setString(1, movie);
			sqlDates = getDates.executeQuery();

			while (sqlDates.next()) {
				dates.add(sqlDates.getDate("showDate").toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dates;

	}

	public Performance getPerformance(String movieName, String date) {
		PreparedStatement prep;
		ResultSet rs;
		try {
			prep = conn.prepareStatement(performanceQuery);
			prep.setString(1, movieName);
			prep.setString(2, date);
			rs = prep.executeQuery();
			while (rs.next()) {
				String theaterName = rs.getString("theaterName");
				String freeSeats = rs.getString("freeSeats");
				Performance performance = new Performance(movieName,
						theaterName, Date.valueOf(date),
						Integer.parseInt(freeSeats));
				return performance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Books a ticket for the performance
	 * 
	 * @param performance
	 * 
	 *            bookQuery = "INSERT INTO Tickets values(null, ?, ?, ?)";
	 *            freeSeatsQuery =
	 *            "SELECT freeSeats FROM performances WHERE movieName = ? and showDate = ?"
	 *            ; setFreeSeatsQuery =
	 *            "UPDATE Performances set freeSeats = ? WHERE movieName = ? and showDate = ?"
	 *            ;
	 */
	public boolean bookTicket(String owner, String movieName, String date) {

		ResultSet ticket;
		PreparedStatement bookingQuery;
		PreparedStatement getFreeSeats;
		PreparedStatement setFreeSeats;
		int freeSeats;
		try {

			getFreeSeats = conn.prepareStatement(freeSeatsQuery);
			getFreeSeats.setString(1, movieName);
			getFreeSeats.setDate(2, Date.valueOf(date));

			setFreeSeats = conn.prepareStatement(setFreeSeatsQuery);
			setFreeSeats.setString(2, movieName);
			setFreeSeats.setDate(3, Date.valueOf(date));

			bookingQuery = conn.prepareStatement(bookQuery);
			bookingQuery.setString(1, owner);
			bookingQuery.setString(2, movieName);
			bookingQuery.setDate(3, Date.valueOf(date));

			ticket = getFreeSeats.executeQuery();

			conn.setAutoCommit(false);
			ticket.next();
			freeSeats = ticket.getInt(1);
			if (freeSeats < 1) {
				conn.rollback();
				return false;
			}

			setFreeSeats.setInt(1, freeSeats - 1);
			setFreeSeats.executeUpdate();
			bookingQuery.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println(e);
			return false;
		}
		return true;

	}

	public LinkedList<String> getMovieDates(String movieName) {
		PreparedStatement getDates;
		ResultSet sqlDates;
		LinkedList<String> dates = new LinkedList<String>();

		try {
			getDates = conn.prepareStatement(dateQuery);
			getDates.setString(1, movieName);
			sqlDates = getDates.executeQuery();
			while (sqlDates.next()) {
				dates.add(sqlDates.getString("showDate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dates;
	}

}
