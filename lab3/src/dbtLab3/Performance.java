package dbtLab3;

import java.sql.Date;

public class Performance {
	
	String movieName, theaterName;
	Date date;
	int nbrOfSeats;
	
	public Performance(String movieName, String theaterName, Date date, int nbrOfSeats){
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.date = date;
		this.nbrOfSeats = nbrOfSeats;
	}

	public String getTheater() {
		return theaterName;
	}

	public int getNbrSeats() {
		return nbrOfSeats;
	}
}
