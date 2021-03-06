
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class BackEnd {

	private Connection conn;
	private String createPalletQuery;
	private String createBatchQuery;
	private String getBatchInfoQuery;
	private String getPalletInfoQuery;
	private String getBatchesQuery;
	private String getCookiesQuery;
	private String blockBatchQuery;
	private String createPalletsInBatchQuery;
	private String getBlockedPalletsQuery;
	private String setBlockedPalletsQuery;
	private String movePalletToStorageQuery;
	private String searchByStatusQuery;
	private String getPalletsInBatchQuery; 
	private String searchByCookieQuery;
	private String searchByDateQuery;
	private String palletExistsQuery;
	private String getCustomersQuery;
	private String getOrderInfoQuery;
	private String getAmountQuery;
	private String updateRawMaterialQuery;
	private String getCustomerOrderQuery;
	private String getPalletOrderInfoQuery;
	private String getPalletNumberQuery;
	private String updateMovePalletQuery;
	private String deletePalletOrderQuery;
	private String updateOrderingQuery;
	private String getPalletStatusInfoQuery;
	private String checkBatchQuery;
	private String getPassPalletQuery;
	private String setPassedQuery;
	private String checkBatchUnpassedQuery;
	private String getDeliverydateQuery; 
	private String getCustomerInfoQuery;
	
	public BackEnd(){
		conn = null;
		
		createBatchQuery = "insert into ProductionBatch values( default, ?, now(), 'Untested')";
		getBatchInfoQuery = "select cookieName, prodDate, QA from ProductionBatch where batchNumber = ?";
		getPalletInfoQuery = "select batchNumber, orderNumber, status, cookieName, prodDate, QA  from Pallet natural join PalletsInBatch natural join productionBatch where palletNumber = ?";
		getBatchesQuery = "select batchNumber, cookieName from ProductionBatch";
		getCookiesQuery = "select cookieName from Recipe";
		blockBatchQuery = "update productionBatch set QA = 'blocked' where batchNumber = ?";
		createPalletQuery = "insert into Pallet values(default, null, 'in production', null)";
		createPalletsInBatchQuery = "insert into PalletsInBatch values(?,?)";
		getBlockedPalletsQuery = "select palletNumber from PalletsInBatch where batchNumber = ?";
		setBlockedPalletsQuery = "update Pallet set status = 'blocked' where palletNumber = ?";
		movePalletToStorageQuery = "update Pallet set status = 'in storage', prodTime = now() where palletNumber = ?";
		getPalletsInBatchQuery = "select palletNumber from PalletsInBatch where batchNumber = ?";
		searchByStatusQuery = "select batchNumber, palletNumber, cookieName, QA from Pallet natural join PalletsInBatch natural join productionBatch where status = ? order by batchNumber, palletNumber";
		searchByCookieQuery = "select batchNumber, palletNumber, status, QA from Pallet natural join PalletsInBatch natural join productionBatch where cookieName = ? order by batchNumber, palletNumber";
		searchByDateQuery = "select batchNumber, palletNumber, cookieName, status, QA, prodDate from Pallet natural join PalletsInBatch natural join productionBatch where prodDate > ? and prodDate < ? order by batchNumber, palletNumber";
		palletExistsQuery = "select * from Pallet where palletNumber = ?";
		getCustomersQuery = "select customerName from Customer";
		getOrderInfoQuery = "select * from Ordering where orderNumber = ?";
		getAmountQuery = "select RawMaterial.amount, cookieContains.amount, RawMaterial.ingredientName from RawMaterial join cookieContains where cookieContains.cookieName = ? and RawMaterial.ingredientName = cookieContains.ingredientName;";
		updateRawMaterialQuery = "update Rawmaterial set amount = amount - ? where ingredientName = ?";
		getCustomerOrderQuery = "select orderNumber, palletNumber, deliveryDate from Ordering natural join Pallet where customerName = ?";
		getPalletOrderInfoQuery = "select cookieName, nbrOfPallets from PalletOrder where orderNumber = ?";
		getPalletNumberQuery = "select palletNumber from Pallet natural join PalletsInBatch natural join productionBatch where cookieName = ? and status='in storage' and QA = 'passed'";
		updateMovePalletQuery = "update Pallet set status = 'delivered', orderNumber = ? where palletNumber = ?";
		deletePalletOrderQuery = "delete from PalletOrder where orderNumber = ? and cookieName = ?";
		updateOrderingQuery = "update Ordering set deliveryDate = now() where orderNumber = ? ";
		getPalletStatusInfoQuery = "select * from Pallet where status = 'in production' and palletNumber = ?";
		checkBatchQuery = "select * from productionBatch where batchNumber = ? and QA = 'untested'";
		getPassPalletQuery = "select palletNumber from Pallet natural join PalletsInBatch where batchNumber = ?";
		setPassedQuery = "update productionBatch set QA='passed' where batchNumber = ? ";
		checkBatchUnpassedQuery = "select * from productionBatch where batchNumber = ? and QA = 'untested'";
		getDeliverydateQuery = "select deliveryDate from Ordering where orderNumber = ? and deliveryDate is null";
		getCustomerInfoQuery = "select deliveryDate, customerName from Ordering where orderNumber = ?";
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
			sb.append("Batch number " + batchNbr + " has ");
			sb.append(batchInfoSet.getString("cookieName") + ". " + "\n");
			sb.append("It was produced on " + batchInfoSet.getString("prodDate") + " and has the QA result: ");
			sb.append(batchInfoSet.getString("QA") + "\n");
			sb.append("The following pallets are in the batch: \n");
			sb.append(getPalletsInBatch(batchNbr));
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
			if(palletInfoSet.next()){
			sb.append("Pallet number " + palletNbr + " was made in batch " + Integer.toString(palletInfoSet.getInt("batchNumber")));
			int orderNbr = palletInfoSet.getInt("orderNumber");
			if( orderNbr != 0){
				PreparedStatement checkCustomer = conn.prepareStatement(getCustomerInfoQuery);
				checkCustomer.setInt(1, orderNbr);
				ResultSet customer = checkCustomer.executeQuery();
				if(customer.next()){
					sb.append("\n It has order number " + orderNbr + " and was delivered " + customer.getTimestamp("deliveryDate").toString() + " to " + customer.getString("customerName"));
				}
			}
			sb.append("\nIt's status is currently " + palletInfoSet.getString("status") + " it was produced " + palletInfoSet.getDate("prodDate")); 
			}
			sb.append("\nIt contains " + palletInfoSet.getString("cookieName") + " and has the QA-result: " + palletInfoSet.getString("QA"));
		} catch(SQLException e) {
			System.err.println(e);
		}
		return sb.toString();
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
	
	public ArrayList<String> getCustomers() {
		ArrayList<String> customers = new ArrayList<String>();
 		PreparedStatement getCustomers;
 		ResultSet rs;
 		try {
 		getCustomers= conn.prepareStatement(getCustomersQuery);
 			rs = getCustomers.executeQuery();
 			while(rs.next()) {
 				customers.add(rs.getString(1));
 			}
 		} catch(SQLException e) {
 			System.err.println(e);
 		}
 		return customers;
  	}
	
	public ArrayList<Integer> createBatch(String cookieName, int nbrOfPallets){
		PreparedStatement createBatch;
		ArrayList<Integer> palletList = new ArrayList<Integer>();
		int batchNbr;
		
		try {
			conn.setAutoCommit(false);
			
			PreparedStatement getAmounts = conn.prepareStatement(getAmountQuery);
			getAmounts.setString(1, cookieName);
			ResultSet amounts = getAmounts.executeQuery();
			
			
			while(amounts.next()){ //checks that the required amount is in stock 
				int neededAmount = amounts.getInt("cookieContains.amount") * nbrOfPallets *54;
				if(neededAmount > amounts.getInt("RawMaterial.amount")){
					conn.rollback();
					return null;
				}
				PreparedStatement updateStock = conn.prepareStatement(updateRawMaterialQuery);
				updateStock.setInt(1, neededAmount);
				updateStock.setString(2,  amounts.getString("RawMaterial.ingredientName"));
				updateStock.executeUpdate();
			}
			
			createBatch = conn.prepareStatement(createBatchQuery, Statement.RETURN_GENERATED_KEYS);
			createBatch.setString(1, cookieName);
			createBatch.executeUpdate();
			ResultSet batchResult = createBatch.getGeneratedKeys();
			
			if(batchResult.next()){
				batchNbr = batchResult.getInt(1);
				for(int i = 0; i < nbrOfPallets ; i++){
					palletList.add(createPallet(cookieName, batchNbr));
				}
			}

		conn.commit();
		conn.setAutoCommit(true);
		}catch(SQLException e) {
			System.err.println(e);
			try{
				conn.rollback();
			} catch( SQLException e2){
				System.err.println(e2);
				return null;
			}
			
			return null;
		}
		
		
		
		
		return palletList;
		
	}

	public Vector<Vector<String>> searchByStatus(String status){
		Vector<Vector<String>> list = new Vector<Vector<String>>();
		try{
		PreparedStatement searchStmt = conn.prepareStatement(searchByStatusQuery);
		searchStmt.setString(1, status);
		ResultSet searchResult = searchStmt.executeQuery();
		while(searchResult.next()){
			Vector<String> row = new Vector<String>();
			row.add(Integer.toString(searchResult.getInt("batchNumber")));
			row.add(Integer.toString(searchResult.getInt("palletNumber")));
			row.add(searchResult.getString("cookieName"));
			row.add(searchResult.getString("QA"));

			list.add(row);
		}
		
		}catch(SQLException e){
			System.err.println(e);
		}
		
		return list;
	}
	
	public Vector<Vector<String>> searchByCookie(String cookieName){
		Vector<Vector<String>> list = new Vector<Vector<String>>();
		try{
		PreparedStatement searchStmt = conn.prepareStatement(searchByCookieQuery);
		searchStmt.setString(1, cookieName);
		ResultSet searchResult = searchStmt.executeQuery();
		while(searchResult.next()){
			Vector<String> row = new Vector<String>();
			row.add(Integer.toString(searchResult.getInt("batchNumber")));
			row.add(Integer.toString(searchResult.getInt("palletNumber")));
			row.add(searchResult.getString("status"));
			row.add(searchResult.getString("QA"));

			list.add(row);
		}
		
		}catch(SQLException e){
			System.err.println(e);
		}
		
		return list;
	}
	
	public Vector<Vector<String>> searchByDate(java.sql.Date startDate, java.sql.Date endDate){
		Vector<Vector<String>> list = new Vector<Vector<String>>();
		try{
		PreparedStatement searchStmt = conn.prepareStatement(searchByDateQuery);
		searchStmt.setDate(1, startDate);
		searchStmt.setDate(2,  endDate);
		ResultSet searchResult = searchStmt.executeQuery();
		while(searchResult.next()){
			Vector<String> row = new Vector<String>();
			row.add(Integer.toString(searchResult.getInt("batchNumber")));
			row.add(Integer.toString(searchResult.getInt("palletNumber")));
			row.add(searchResult.getString("cookieName"));
			row.add(searchResult.getString("status"));
			row.add(searchResult.getString("QA"));
			row.add(searchResult.getDate("prodDate").toString());

			list.add(row);
		}
		
		}catch(SQLException e){
			System.err.println(e);
		}
		
		return list;
	}
	
	public Vector<Vector<String>> searchByCustomer(String customer){
		
		Vector<Vector<String>> list = new Vector<Vector<String>>();
		
		try{
			PreparedStatement searchByCustomer = conn.prepareStatement(getCustomerOrderQuery);
			searchByCustomer.setString(1, customer);
			ResultSet searchResult = searchByCustomer.executeQuery();
			while(searchResult.next()){
				Vector<String> row = new Vector<String>();
				row.add(Integer.toString(searchResult.getInt("orderNumber")));
				row.add(Integer.toString(searchResult.getInt("palletNumber")));
				row.add(searchResult.getTimestamp("deliveryDate").toString());
				list.add(row);
			}
			
		}catch(SQLException e){
			System.err.println(e);
			return null;
		}
		
		
		
		return list;
		
	}
	
	public ArrayList<Integer> movePalletToDelivered(int orderNbr){
		
		String cookieName;
		ArrayList<Integer> palletList = new ArrayList<Integer>();
		boolean orderExistsFlag = false;

		try{
			PreparedStatement checkOrder = conn.prepareStatement(getDeliverydateQuery);
			checkOrder.setInt(1, orderNbr);
			if(!checkOrder.executeQuery().next()){
				return null;
			}
			PreparedStatement palletOrder = conn.prepareStatement(getPalletOrderInfoQuery);
			palletOrder.setInt(1, orderNbr);
			ResultSet palletOrders = palletOrder.executeQuery();
			conn.setAutoCommit(false);
			while(palletOrders.next()){
				orderExistsFlag = true;
				cookieName = palletOrders.getString("cookieName");
				int neededPallets = palletOrders.getInt("nbrOfPallets");
				PreparedStatement palletNbr = conn.prepareStatement(getPalletNumberQuery);
				palletNbr.setString(1, cookieName);
				ResultSet palletNbrResult = palletNbr.executeQuery();
				
				
				int palletCount= 0;
				while(palletNbrResult.next() && palletCount < neededPallets){
					int palletId = palletNbrResult.getInt(1);
					palletList.add(palletId);
					PreparedStatement movePalletStmt = conn.prepareStatement(updateMovePalletQuery);
					movePalletStmt.setInt(1, orderNbr);
					movePalletStmt.setInt(2,  palletId);
					movePalletStmt.executeUpdate();
					palletCount++;
				}
				if (palletCount < neededPallets){
					conn.rollback();
					palletList.clear();
					return palletList;
				}
				PreparedStatement deletePalletOrder = conn.prepareStatement(deletePalletOrderQuery);
				deletePalletOrder.setInt(1, orderNbr);
				deletePalletOrder.setString(2, cookieName);
				deletePalletOrder.executeUpdate();
			}
			PreparedStatement updateOrdering = conn.prepareStatement(updateOrderingQuery);
			updateOrdering.setInt(1,  orderNbr);
			updateOrdering.executeUpdate();
				
				
				
			
			conn.commit();
			conn.setAutoCommit(true);
			}catch(SQLException e){
	 			System.err.println(e);
	 			try{
	 				conn.rollback();
	 			}catch(SQLException e2){
	 				System.err.println(e2);
	 				return null;
	 			}
	 			return null;
	 			
			}
			if(orderExistsFlag){
				return palletList;
			} else {
				return null;
			}
	}
	
	public boolean movePalletToStorage(int palletNbr){
		
		if(!palletExists(palletNbr)){
			return false;
		}
		
		try{
		PreparedStatement movePalletStmt = conn.prepareStatement(movePalletToStorageQuery);
		movePalletStmt.setInt(1,  palletNbr);
		movePalletStmt.execute();
		}catch(SQLException e){
 			System.err.println(e);
 			return false;
		}
		
		return true;
	}
	
	public boolean orderExist(int orderNbr) {
		PreparedStatement checkOrders;
		ResultSet rs;
		try {
			checkOrders = conn.prepareStatement(getOrderInfoQuery);
			checkOrders.setInt(1, orderNbr);
			rs = checkOrders.executeQuery();
			if(rs.next()){
				return true;
			}
		}catch(SQLException e) {
			System.err.println(e);
		}
		return false;
	}

	public boolean batchExist(int batchNbr) {
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

	public boolean palletExists(int palletNbr){
		try{
			PreparedStatement palletExists = conn.prepareStatement(palletExistsQuery);
			palletExists.setInt(1,  palletNbr);
			ResultSet palletExistsResult = palletExists.executeQuery();
			if(palletExistsResult.next()){
				return true;
			}
		}catch(SQLException e){
			System.err.print(e);
		}
		return false;

	}
	
	public boolean palletInProd(int palletNbr) {
		try{
		PreparedStatement checkIfInProd = conn.prepareStatement(getPalletStatusInfoQuery);
		checkIfInProd.setInt(1, palletNbr);
		ResultSet isInProd = checkIfInProd.executeQuery();
		
		if(isInProd.next()){
			return true;
		}
		
		}catch(SQLException e){
			System.err.println(e);
			return false;
		}
		
		return false;
	}
	
	public ArrayList<Integer> passBatch(int batchNbr){
		
		ArrayList<Integer> palletList = new ArrayList<Integer>();

		if(!batchExist(batchNbr)) {
			return palletList;
		}
		
		try{
			PreparedStatement checkBatch = conn.prepareStatement(checkBatchUnpassedQuery);
			checkBatch.setInt(1,  batchNbr);
			if(!checkBatch.executeQuery().next()){
				return palletList;
			}
			
			PreparedStatement passed = conn.prepareStatement(setPassedQuery);
			passed.setInt(1,  batchNbr);
			passed.executeUpdate();
		
			PreparedStatement getPalletIds = conn.prepareStatement(getPassPalletQuery);
			getPalletIds.setInt(1, batchNbr);
			ResultSet palletIds = getPalletIds.executeQuery();
			while(palletIds.next()){
			palletList.add(palletIds.getInt(1));
		}
		
		} catch(SQLException e){
			System.err.println(e);
			return null;
		}
		
		return palletList;
	}

	public ArrayList<Integer> blockBatch(int batchNbr) {
		PreparedStatement blockBatches;
		ArrayList<Integer> blockedPalletsNbr = new ArrayList<Integer>();

		if(!batchExist(batchNbr)) {
			return blockedPalletsNbr;
		}
		try {
			
			PreparedStatement checkBatch = conn.prepareStatement(checkBatchQuery);
			checkBatch.setInt(1,  batchNbr);
			if(!checkBatch.executeQuery().next()){
				return blockedPalletsNbr;
			}
			
			
			blockBatches = conn.prepareStatement(blockBatchQuery);
			blockBatches.setInt(1, batchNbr);
			blockBatches.executeUpdate();
		
		
		PreparedStatement blockPalletsNbr = conn.prepareStatement(getBlockedPalletsQuery);
		blockPalletsNbr.setInt(1, batchNbr);
		ResultSet blockResult = blockPalletsNbr.executeQuery();
		while(blockResult.next()){
			blockedPalletsNbr.add(blockResult.getInt(1));
		}
		
		PreparedStatement blockPallets = conn.prepareStatement(setBlockedPalletsQuery);
		for(int palletNbr : blockedPalletsNbr){
			blockPallets.setInt(1,  palletNbr);
			blockPallets.executeUpdate();
		}
		
		
		}catch(SQLException e) {
			System.err.println(e);
		}
		return blockedPalletsNbr;

	}
	
	private String getPalletsInBatch(int batchNbr) {
		PreparedStatement getPallets;
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		try {
			getPallets = conn.prepareStatement(getPalletsInBatchQuery);
			getPallets.setInt(1, batchNbr);
			rs = getPallets.executeQuery();
			while(rs.next()) {
				sb.append(rs.getString(1) + ", ");
			}
		} catch(SQLException e) {
			System.err.println(e);
		}
		return sb.toString();
	}
	
	private int createPallet(String recipe, int batchNbr){
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
			return palletNbr;					
			
		}
		} catch(SQLException e) {
			System.err.println(e);
			return -1;
		}
		return -1 ;
	}
				
}
