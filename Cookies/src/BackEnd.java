
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
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
	private String createPalletsInBatchQuery;
	private String getStatusQuery;

	public BackEnd(){
		conn = null;
		
//		createPalletQuery = "Insert into Pallets values (null,?,created)";
//		getNbrOfBatchesQuery = "select ";
		createBatchQuery = "insert into ProductionBatch values( default, ?, now(), 'Untested')";
		getIngredientAmountRecipeQuery = "select amount from RawMaterial where ingredientName = ?";
		getIngredientsQuery = "select ingredientName, amount from CookieContains where cookieName = ?";
		updateMaterialQuery = "update RawMaterial set amount = amount - ? where ingredientName = ?";
		getMaterialAmountQuery = "select amount from RawMaterial where ingredientName = ?";
		getBatchInfoQuery = "select cookieName, prodDate, QA from ProductionBatch where batchNumber = ?";
		getPalletInfoQuery = "select orderNumber, status from Pallet where palletNumber = ?";
		getBatchesQuery = "select batchNumber, cookieName from ProductionBatch";
		getCookiesQuery = "select cookieName from Recipe";
		blockBatchQuery = "update productionBatch set QA = 'blocked' where batchNumber = ?";
		createPalletQuery = "insert into Pallet values(default, null, 'in production')";
		createPalletsInBatchQuery = "insert into PalletsInBatch values(?,?)";
		getStatusQuery = "select status from Pallet group by status";

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
		HashMap<String ,Double> ingredientMap = getIngredients(cookieName, nbrOfPallets);
		HashMap<String, Double> amountExists = new HashMap<String, Double>();
		ArrayList<Integer> palletList = new ArrayList<Integer>();
		int batchNbr;
		
		// Checks that the required amount is in stock, and puts the new stock values in the hashmap amountExists
		for(Entry<String, Double> entry : ingredientMap.entrySet()){
			double stockAmount = getRawStock(entry.getKey());
			if(stockAmount == -1 || stockAmount < entry.getValue()){
				System.out.println("Not enough material in storage");
				return null;
			}
			stockAmount =  entry.getValue();
			amountExists.put(entry.getKey(), stockAmount);
		}
		
		updateRawMaterial(amountExists);
		
		
		try {
			
			System.out.println("creating batch");
			//creates the batch
			createBatch = conn.prepareStatement(createBatchQuery, Statement.RETURN_GENERATED_KEYS);
			createBatch.setString(1, cookieName);
			createBatch.executeUpdate();
			ResultSet batchResult = createBatch.getGeneratedKeys();
			
			if(batchResult.next()){
				batchNbr = batchResult.getInt(1);
				System.out.println("batch created, batchNbr" + batchNbr);
				//creates the pallets for the specified batch
				for(int i = 0; i < nbrOfPallets ; i++){
					palletList.add(createPallet(cookieName, batchNbr));
				}
			}

		}catch(SQLException e) {
			System.err.println(e);
			return null;
		}
		
		
		
		
		return palletList;
		
	}
	
	private double getRawStock(String ingredientName) {
		PreparedStatement getAmount;
		ResultSet amountResult;
		double amount = -1;
		try {
			getAmount = conn.prepareStatement(getMaterialAmountQuery);
			getAmount.setString(1, ingredientName);
			
			amountResult = getAmount.executeQuery();
			if(amountResult.next()){
				amount = amountResult.getDouble("amount");
			}
		} catch(SQLException e) {
			System.err.println(e);
		}
		return amount;
	}

	public HashMap<String, Double> getIngredients(String cookieName, int nbrOfPallets) {
		System.out.println("getting ingredients");
		PreparedStatement getIngredients;
		HashMap<String, Double> ingredientList = new HashMap<String, Double>();
		ResultSet ingredients;
		try{
			getIngredients = conn.prepareStatement(getIngredientsQuery);
			getIngredients.setString(1, cookieName);
			
			ingredients = getIngredients.executeQuery();
			
			while(ingredients.next()){
				ingredientList.put(ingredients.getString("ingredientName"), ingredients.getDouble("amount")*54*nbrOfPallets);
			}	
		}catch(SQLException e) {
			System.err.println(e);
			return null;
		}
		return ingredientList;
	}
	
	public void updateRawMaterial(HashMap<String, Double> amount ){
		PreparedStatement updateMaterial;
		try{
			updateMaterial = conn.prepareStatement(updateMaterialQuery);
			for(Entry<String,Double> entry : amount.entrySet()){
		
				updateMaterial.setDouble(1, entry.getValue());
				updateMaterial.setString(2, entry.getKey());
				updateMaterial.execute();

			}
			
		}catch(SQLException e) {
			System.err.println(e);
		}
	}
	
	public int createPallet(String recipe, int batchNbr){
		try{
		
		PreparedStatement palletStmt = conn.prepareStatement(createPalletQuery, Statement.RETURN_GENERATED_KEYS);
		palletStmt.execute();
		ResultSet palletId = palletStmt.getGeneratedKeys();
		
		if(palletId.next()){
			int palletNbr = palletId.getInt(1);
			
			PreparedStatement PalInBatchStmt = conn.prepareStatement(createPalletsInBatchQuery);
			PalInBatchStmt.setInt(1,  palletNbr);
			PalInBatchStmt.setInt(2,  batchNbr);
			PalInBatchStmt.execute();
			System.out.println(palletNbr);
			return palletNbr;			
			
			
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
			blockBatches.execute();
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
				cookies.add(cookieSet.getString(1));
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return cookies;
	}
	
	public void movePalletToStorage(int palletNbr){
		//TODO : this should move pallets from "in production" to "in storage"
		return;
	}

	public ArrayList<String> getStatuses() {
 		ArrayList<String> statuses = new ArrayList<String>();
 		PreparedStatement getStatuses;
 		ResultSet statusSet;
 		try {
 		getStatuses = conn.prepareStatement(getStatusQuery);
 			statusSet = getStatuses.executeQuery();
 			while(statusSet.next()) {
 				statuses.add(statusSet.getString(1));
 		}
 		} catch(SQLException e) {
 			System.err.println(e);
 	}
 		return statuses;
  	}
}
