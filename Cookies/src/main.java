package KrustyKookies;
import java.util.Vector;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database db = new Database();
		new GUI(db);


		BackEnd be = new BackEnd();

		be.openConnection();

		Vector<String> list = be.getRecipes();

		for(String recipe : list){
			System.out.println(recipe);
		}
	}

}
