
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.print.DocFlavor.READER;





public class BackEnd {


	private Connection conn;
	private String createPalletQuery;
	private String getNbrOfBatchesQuery;
	private String createBatchQuery;
	private String getIngredientAmountRecipeQuery;
	private String getIngredientsQuery;
	private String updateMaterialQuery;
	private String getMaterialAmountQuery;
	private String getBatchInfoQuery;
	private String getPalletInfoQuery;
	private String getBatchesQuery;
	private String getCookiesQuery;
	private String blockBatchQuery;

	public BackEnd(){
		conn = null;
		
//		createPalletQuery = "Insert into Pallets values (null,?,created)";
//		getNbrOfBatchesQuery = "select ";
		createBatchQuery = "insert into ProductionBatch values( ?, now(), Untested)";
		getIngredientAmountRecipeQuery = "select amount from RawMaterial where ingredientName = ?";
		getIngredientsQuery = "select ingredientName from CookieContains where cookieName = ?";
		updateMaterialQuery = "update RawMaterial set amount = amount - ? where ingredientName = ?";
		getMaterialAmountQuery = "select amount from RawMaterial where ingredientName = ?";
		getBatchInfoQuery = "select cookieName, prodDate, QA from ProductionBatch where batchNumber = ?";
		getPalletInfoQuery = "select orderNumber, status from Pallet where palletNumber = ?";
		getBatchesQuery = "select batchNumber, cookieName from ProductionBatch";
		getCookiesQuery = "select cookieName from Recipe";
		blockBatchQuery = "update productionBatch set QA = 'blocked' where batchNumber = ?";
		createPalletQuery = "insert into Pallets values(null, 'in production')";
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

	
	public ArrayList<Integer> createBatch(String cookieName, int nbrOfPallets){
		PreparedStatement createBatch;
		PreparedStatement getIngredientAmount;
		ResultSet amountSet;
		ArrayList<String> ingredientList = getIngredients(cookieName);
		HashMap<String ,Double> amountExists = new HashMap<String, Double>();
		ArrayList<Integer> palletList = new ArrayList<Integer>();
		try {
			for(int i = 0; i < ingredientList.size(); i ++) {
				getIngredientAmount = conn.prepareStatement(getIngredientAmountRecipeQuery);
				getIngredientAmount.setString(1, ingredientList.get(i));
				amountSet = getIngredientAmount.executeQuery();
				amountSet.next();
				double amount = amountSet.getDouble("amount");
				amount = amount*54*nbrOfPallets;
				if (amount < requiredAmount(cookieName) || requiredAmount(cookieName) == -1){
					return null;
				}else {
					amountExists.put(ingredientList.get(i), amount);					
				}
			}
			
			//skapa batch
			createBatch = conn.prepareStatement(createBatchQuery);
			createBatch.executeQuery();
			
			for(int i = 0; i < ingredientList.size(); i ++) {
				double amount = amountExists.get(i);
				updateRawMaterial(ingredientList.get(i), amount*54*nbrOfPallets);
			}
		}catch(SQLException e) {
			System.err.println(e);
			return null;
		}
		
		for(int i = 0; i < nbrOfPallets ; i++){
			palletList.add(createPallet(cookieName));
		}
		
		
		return palletList;
		
	}
	
	private double requiredAmount(String cookieName) {
		PreparedStatement getAmount;
		ResultSet amountSet;
		double amount = -1;
		try {
			getAmount = conn.prepareStatement(getMaterialAmountQuery);
			getAmount.setString(1, cookieName);
			
			amountSet = getAmount.executeQuery();
			amountSet.next();
			amount = amountSet.getDouble("amount");
		} catch(SQLException e) {
			System.err.println(e);
		}
		return amount;
	}

	public ArrayList<String> getIngredients(String cookieName) {
		PreparedStatement getIngredients;
		ArrayList<String> ingredientList = new ArrayList<String>();
		ResultSet ingredients;
		try{
			getIngredients = conn.prepareStatement(getIngredientsQuery);
			getIngredients.setString(1, cookieName);
			
			ingredients = getIngredients.executeQuery();
			
			while(ingredients.next()){
				ingredientList.add(ingredients.getString("ingredientName"));
			}	
		}catch(SQLException e) {
			System.err.println(e);
			return null;
		}
		return ingredientList;
	}
	
	public void updateRawMaterial(String ingredientName, double amount){
		PreparedStatement updateMaterial;
		try{
			updateMaterial = conn.prepareStatement(updateMaterialQuery);
			updateMaterial.setDouble(1, amount);
			updateMaterial.setString(2, ingredientName);
			
			updateMaterial.execute();
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public int createPallet(String recipe){
		try{
		
		PreparedStatement palletStmt = conn.prepareStatement(createPalletQuery);
		palletStmt.executeQuery();
		ResultSet palletId = palletStmt.getGeneratedKeys();
		
		if(palletId.next()){
			return palletId.getInt(1);
		}
		} catch(SQLException e) {
			System.err.println(e);
			return -1;
		}
		return -1 ;
	}



	public String getBatchInfo(int batchNbr) {
		PreparedStatement getBatchInfo;
		ResultSet batchInfoSet;
		StringBuilder sb = new StringBuilder();
		if(!batchExist(batchNbr)) {
			return null;
		}
		try {
			getBatchInfo = conn.prepareStatement(getBatchInfoQuery);
			getBatchInfo.setInt(1, batchNbr);
			
			batchInfoSet = getBatchInfo.executeQuery();
			batchInfoSet.next();
			sb.append("Batch number" + batchNbr + " has ");
			sb.append(batchInfoSet.getString("cookieName") + " and was produced on ");
			sb.append(batchInfoSet.getString("prodDate") + " and has the QA result: ");
			sb.append(batchInfoSet.getString("QA"));
		} catch(SQLException e) {
			System.err.println(e);
		}
		return sb.toString();
	}

	public String getPalletInfo(int palletNbr) {
		PreparedStatement getPalletInfo;
		ResultSet palletInfoSet;
		StringBuilder sb = new StringBuilder();
		try {
			getPalletInfo = conn.prepareStatement(getPalletInfoQuery);
			getPalletInfo.setInt(1, palletNbr);
			
			palletInfoSet = getPalletInfo.executeQuery();
			palletInfoSet.next();
			sb.append("Pallet number" + palletNbr + " belongs to order number ");
			sb.append(palletInfoSet.getString("orderNumber") + " and has status: ");
			sb.append(palletInfoSet.getString("status"));
		} catch(SQLException e) {
			System.err.println(e);
		}
		return sb.toString();
	}

	public boolean blockBatch(int batchNbr) {
		PreparedStatement blockBatches;
		if(!batchExist(batchNbr)) {
			return false;
		}
		try {
			blockBatches = conn.prepareStatement(blockBatchQuery);
			blockBatches.setInt(1, batchNbr);
			blockBatches.executeQuery();
		}catch(SQLException e) {
			System.err.println(e);
		}
		return true;
	}

	private boolean batchExist(int batchNbr) {
		PreparedStatement checkBatches;
		ResultSet checkBatchSet;
		try {
			checkBatches = conn.prepareStatement(getBatchInfoQuery);
			checkBatches.setInt(1, batchNbr);
			checkBatchSet = checkBatches.executeQuery();
			if(!checkBatchSet.next()){
				return false;
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return true;
	}

	public ArrayList<String> getBatches() {
		PreparedStatement getBatches;
		ResultSet batchSet;
		ArrayList<String> batches = new ArrayList<String>();
		try {
			getBatches = conn.prepareStatement(getBatchesQuery);
			batchSet = getBatches.executeQuery();
			while(batchSet.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(batchSet.getInt("batchNumber") + " -- " + batchSet.getString("cookieName"));
				batches.add(sb.toString());
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return batches;
	}

	public ArrayList<String> getCookies() {
		PreparedStatement getCookies;
		ResultSet cookieSet;
		ArrayList<String> cookies = new ArrayList<String>();
		try {
			getCookies = conn.prepareStatement(getCookiesQuery);
			cookieSet = getCookies.executeQuery();
			while(cookieSet.next()) {
				StringBuilder sb = new StringBuilder();
				sb.append(cookieSet.getString("cookieName"));
				cookies.add(sb.toString());
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return cookies;
	}
}