package KrustyKookies;

import java.util.Vector;


public class main {
	


	public static void main(String[] args){


		BackEnd database = new BackEnd();
		database.openConnection();

		Vector<String> list = database.getRecipes();

		for(String recipe : list){
			System.out.println(recipe);
		}


	}










}