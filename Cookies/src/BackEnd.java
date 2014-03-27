
import java.sql.*;
import java.util.Vector;





public class BackEnd {


	private Connection conn;
	private String createPalletQuery;
	private String getNbrOfBatchesQuery;

	public BackEnd(){
		conn = null;
		
		createPalletQuery = "Insert into Pallets values (null,?,created)";
		getNbrOfBatchesQuery = "select ";
		
	}
	
	public boolean openConnection() {
		String userName = "db06";
		String password = "kodord";
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
	
		public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}


	public boolean isConnected() {
		return conn != null;
	}
	
	
	
	public Vector<String> getRecipes(){
		
		Vector<String> recList = new Vector<String>();

		try {
			PreparedStatement getRec;
			ResultSet recipes;
			getRec = conn.prepareStatement("select cookieName from Recipe");
			recipes = getRec.executeQuery();
			while(recipes.next()){
				recList.add(recipes.getString("cookieName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
 
		return recList;
		
	}

	
	public void createBatch(String recipe){

		return;
		
	}
	
	public void updateRawMaterial(String recipe, int pallets){
		
		return;
	}
	
	public void createPallet(String recipe, int nbrOfPallets){
		int nbrOfBatches = nbrOfBatches(recipe);
	
		return;
	}

	private int nbrOfBatches(String recipe) {
		
		return 0;
	}
}
		
