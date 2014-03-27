import java.util.Vector;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BackEnd be = new BackEnd();
		
		be.openConnection();
		
		Vector<String> list = be.getRecipes();
		
		for(String recipe : list){
			System.out.println(recipe);
		}
//		Database db = new Database();
		new GUI(be);


	}

}
