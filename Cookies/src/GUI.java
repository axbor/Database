import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.stream.Location;
import javax.xml.ws.handler.MessageContext.Scope;

/**
 * MovieGUI is the user interface to the movie database. It sets up the main
 * window and connects to the database.
 */
public class GUI {
	/**
	 * db is the database object
	 */

	/**
	 * tabbedPane is the contents of the window. It consists of two panes, User
	 * login and Book tickets.
	 */
	private JTextField cookieAmountField;
	private int amount;
	private JList<String> list;
	private JTextField textField;
	private JTextField textField_2;
	private JTextArea textArea_1;
	private JTextArea textArea_3;
	private JTable table;
	private JTable table_1;
	private JTable table_2 = new JTable();
	private JTextField startDateField;
	private JTextField endDateField;
	private JTable timeTable;
	private JTextField deliverTextField;

	/**
	 * Create a GUI object and connect to the database.
	 * 
	 * @param db
	 *            The database.
	 * @wbp.parser.entryPoint
	 */
	public GUI(BackEnd b) {
		final BackEnd be = b;
		JFrame frame = new JFrame("Cookies");	
		frame.setSize(700,500);

		//create Tab /////////////////////////

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane_1, BorderLayout.CENTER);

		JPanel createPanel = new JPanel();
		tabbedPane_1.addTab("Create pallet", null, createPanel, null);
		createPanel.setLayout(null);

		list = new JList<String>();
		list.setModel(new CookieListModel(be));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		createPanel.add(scrollPane);
		scrollPane.setBounds(50, 94, 150, 200);

		cookieAmountField = new JTextField();
		cookieAmountField.setBounds(368, 195, 144, 62);
		createPanel.add(cookieAmountField);
		cookieAmountField.setColumns(10);

		JButton btnOk = new JButton("Create pallet");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = cookieAmountField.getText();
				try{
					amount = Integer.parseInt(text);
					if(amount < 1) {
						JOptionPane.showMessageDialog(null, "Amount has to be bigger than 0");
					}else {
						int index = list.getSelectedIndex();
						ListModel<String> model = list.getModel();
						String cookie = model.getElementAt(index);
						ArrayList<Integer> palletNbrs = be.createBatch(cookie, amount);
				
						StringBuilder sb = new StringBuilder();
						sb.append("\n");
						for(int nbr : palletNbrs){
							
							sb.append(nbr);
							sb.append("\n");
							
						}
						String pallets = sb.toString();
						
						JOptionPane.showMessageDialog(null, "Created " + amount + " pallets of " + cookie + " with pallet-id " +
						pallets);
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}
		});
		btnOk.setBounds(368, 290, 155, 25);
		createPanel.add(btnOk);

		JLabel lblAmountOfPallets = new JLabel("Enter amount of pallets you want to create of selected cookie");
		lblAmountOfPallets.setBounds(218, 114, 448, 44);
		createPanel.add(lblAmountOfPallets);

		JLabel lblChooseACookie = new JLabel("Choose a cookie type to create");
		lblChooseACookie.setBounds(33, 51, 221, 15);
		createPanel.add(lblChooseACookie);
		
		
		//search Tab /////////////////////////
		
		JTabbedPane searchPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.addTab("Search", null, searchPane, null);
		//search cookie Tab /////////////////////////
		
		JPanel searchCookiePanel = new JPanel();
		searchPane.addTab("By cookie", null, searchCookiePanel, null);
		searchCookiePanel.setLayout(null);
		
		final JList<String> cookieList = new JList<String>();
		cookieList.setModel(new CookieListModel(be));
		cookieList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane cookieScrollPane = new JScrollPane();
		cookieScrollPane.setViewportView(cookieList);
		searchCookiePanel.add(cookieScrollPane);
		cookieScrollPane.setBounds(12, 12, 148, 364);		
		
		JButton btnOk_1 = new JButton("Search");
		btnOk_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int index = cookieList.getSelectedIndex();
					ListModel<String> model = cookieList.getModel();
					String cookieName = model.getElementAt(index);
					table = new JTable(buildCookieTableModel(cookieName));
					JOptionPane.showMessageDialog(null, new JScrollPane(table));
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}

			private TableModel buildCookieTableModel(String status) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Pallet Id");
				columnNames.add("Status");
				columnNames.add("Batch Id");
				columnNames.add("QA-result");
				Vector<Vector<String>> data = be.searchByCookie(status);
				return new DefaultTableModel(data, columnNames);
			}
		});
		btnOk_1.setBounds(172, 189, 117, 25);
		searchCookiePanel.add(btnOk_1);
		
		//search status Tab /////////////////////////
		
		JPanel searchStatuspanel = new JPanel();
		searchPane.addTab("By status", null, searchStatuspanel, null);
		searchStatuspanel.setLayout(null);
		
		final JList<String> statusList = new JList<String>();
		statusList.setModel(new StatusListModel(be));
		statusList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		statusList.setBounds(12, 49, 148, 364);
		searchStatuspanel.add(statusList);

		JButton button_3 = new JButton("Search");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int index = statusList.getSelectedIndex();
					ListModel<String> model = statusList.getModel();
					String status = model.getElementAt(index);
					table_2 = new JTable(buildStatusTableModel(status));
					JOptionPane.showMessageDialog(null, new JScrollPane(table_2));
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}

			private TableModel buildStatusTableModel(String status) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Pallet Id");
				columnNames.add("Batch Id");
				columnNames.add("Cookie");
				columnNames.add("QA-result");
				Vector<Vector<String>> data = be.searchByStatus(status);
				return new DefaultTableModel(data, columnNames);
			}
		});
		button_3.setBounds(172, 226, 117, 25);
		searchStatuspanel.add(button_3);
		
		
		
		//search time Tab /////////////////////////
		
		JPanel searchTimepanel = new JPanel();
		searchPane.addTab("By time", null, searchTimepanel, null);
		searchTimepanel.setLayout(null);
		
		JLabel lblEnterTheTime = new JLabel("Enter the production time interval you want to search pallets for");
		lblEnterTheTime.setBounds(89, 25, 504, 15);
		searchTimepanel.add(lblEnterTheTime);
		
		JLabel lblNewLabel_1 = new JLabel("Start date");
		lblNewLabel_1.setBounds(89, 68, 92, 15);
		searchTimepanel.add(lblNewLabel_1);
		
		JLabel lblEndDate = new JLabel("End date");
		lblEndDate.setBounds(212, 68, 70, 15);
		searchTimepanel.add(lblEndDate);
		
		startDateField = new JTextField();
		startDateField.setBounds(66, 95, 114, 19);
		searchTimepanel.add(startDateField);
		startDateField.setColumns(10);
		
		endDateField = new JTextField();
		endDateField.setBounds(203, 95, 114, 19);
		searchTimepanel.add(endDateField);
		endDateField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String startDateString = startDateField.getText();
				String endDateString = endDateField.getText(); 
				try{
					java.sql.Date startDate = java.sql.Date.valueOf(startDateString);
					java.sql.Date endDate = java.sql.Date.valueOf(endDateString);
					timeTable= new JTable(buildTimeTableModel(startDate, endDate));
					JOptionPane.showMessageDialog(null, new JScrollPane(timeTable));
				}catch(IllegalArgumentException err) {
					JOptionPane.showMessageDialog(null, "Date has to be on the form : YYYY-MM-DD");
				}
			}

			private TableModel buildTimeTableModel(java.sql.Date startDate, java.sql.Date endDate) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Batch Id");
				columnNames.add("Pallet Id");
				columnNames.add("Cookie");
				columnNames.add("Status");
				columnNames.add("QA-result");
				columnNames.add("Prod. date");
				Vector<Vector<String>> data = be.searchByDate(startDate, endDate);
				return new DefaultTableModel(data, columnNames);
			}
		});
		btnSearch.setBounds(405, 92, 117, 25);
		searchTimepanel.add(btnSearch);
		
		//search pallet Tab /////////////////////////
		JPanel searchPalletpanel = new JPanel();
		searchPalletpanel.setLayout(null);
		searchPane.addTab("By pallet number", null, searchPalletpanel, null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(125, 106, 213, 82);
		searchPalletpanel.add(textField);
		
		JLabel label = new JLabel("Write pallet number of the pallet you want to search for");
		label.setBounds(55, 68, 428, 15);
		searchPalletpanel.add(label);
		
		JButton button = new JButton("Ok");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textField.getText();
				try{
					int palletNbr = Integer.parseInt(text);
					if(palletNbr < 1) {
						JOptionPane.showMessageDialog(null, "Amount has to be bigger than 0");
						return;
					}
					if(!be.palletExists(palletNbr)) {
						JOptionPane.showMessageDialog(null, "Pallet does not exist");
						return;
					}else {
						String info = be.getPalletInfo(palletNbr);
						textArea_1.setText(info);
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}
		});
		button.setBounds(172, 200, 117, 25);
		searchPalletpanel.add(button);
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setColumns(10);
		textArea_1.setBounds(55, 279, 557, 131);
		searchPalletpanel.add(textArea_1);
		
		JLabel label_1 = new JLabel("Info");
		label_1.setBounds(56, 233, 70, 15);
		searchPalletpanel.add(label_1);
		
		
		//search batch Tab /////////////////////////
		JPanel searchBatchpanel = new JPanel();
		searchBatchpanel.setLayout(null);
		searchPane.addTab("By batch number", null, searchBatchpanel, null);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(125, 106, 213, 82);
		searchBatchpanel.add(textField_2);
		
		JLabel label_2 = new JLabel("Write batch number of the batch you want to search for");
		label_2.setBounds(55, 68, 428, 15);
		searchBatchpanel.add(label_2);
		
		JButton button_1 = new JButton("Ok");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textField_2.getText();
				try{
					int batchNbr = Integer.parseInt(text);
					if(batchNbr < 1) {
						JOptionPane.showMessageDialog(null, "Amount has to be bigger than 0");
						return;
					}
					if(!be.batchExist(batchNbr)) {
						JOptionPane.showMessageDialog(null, "The batch does not exist");
						return;
					}else {
						String info = be.getBatchInfo(batchNbr);
						textArea_3.setText(info);
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}
		});
		button_1.setBounds(172, 200, 117, 25);
		searchBatchpanel.add(button_1);
		
		textArea_3 = new JTextArea();
		textArea_3.setEditable(false);
		textArea_3.setColumns(10);
		textArea_3.setBounds(55, 279, 608, 131);
		searchBatchpanel.add(textArea_3);
		
		JLabel label_3 = new JLabel("Info");
		label_3.setBounds(56, 233, 70, 15);
		searchBatchpanel.add(label_3);


		//search Customer Tab /////////////////////////
		JPanel searchCustomerPanel = new JPanel();
		searchCustomerPanel.setLayout(null);
		searchPane.addTab("By customer", null, searchCustomerPanel, null);
		

		final JList<String> customerList = new JList<String>();
		customerList.setModel(new CustomerListModel(be));
		customerList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		customerList.setBounds(12, 49, 148, 364);
		searchCustomerPanel.add(customerList);

		JButton searchCustomerBtn = new JButton("Search");
		searchCustomerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int index = customerList.getSelectedIndex();
					ListModel<String> model = customerList.getModel();
					String customer = model.getElementAt(index);
					JTable customerTable = new JTable(buildCustomerTableModel(customer));
					JOptionPane.showMessageDialog(null, new JScrollPane(customerTable));
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}

			private TableModel buildCustomerTableModel(String customer) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Pallet id");
				columnNames.add("Delivery date");
				columnNames.add("Delivery time");
				Vector<Vector<String>> data = be.searchByCustomer(customer);
				return new DefaultTableModel(data, columnNames);
			}
		});
		searchCustomerBtn.setBounds(172, 226, 117, 25);
		searchCustomerPanel.add(searchCustomerBtn);



		//blocking panel /////////////////////////////////


		JPanel blockingPanel = new JPanel();
		tabbedPane_1.addTab("Block batch", null, blockingPanel, null);
		blockingPanel.setLayout(null);
		
		
		
		final JTextField blockTextField = new JTextField();
		blockTextField.setBounds(259, 131, 156, 85);
		blockingPanel.add(blockTextField);
		blockTextField.setColumns(10);

		JButton btnBlock = new JButton("Block batch");
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = blockTextField.getText();
				try{
					int number = Integer.parseInt(text);
					if(number < 1) {
						JOptionPane.showMessageDialog(null, "The batch number has to be a positive integer");
						return;
					}
					ArrayList<Integer> blockedPallets = be.blockBatch(number);
					
					if(blockedPallets == null){
						JOptionPane.showMessageDialog(null, "A batch with that number does not exist");
					}else {
						StringBuilder sb = new StringBuilder();
						sb.append("\n");
						for(int nbr : blockedPallets){
							sb.append(nbr);
							sb.append("\n");
						}
						String blockedStr = sb.toString();
						JOptionPane.showMessageDialog(null, "The following pallets where blocked: " + blockedStr);

						
					}
					
					
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The batch number has to be a positive integer");
				}
			}
		});
		btnBlock.setBounds(271, 259, 136, 25);
		blockingPanel.add(btnBlock);


		JLabel lblNewLabel = new JLabel("Enter number of the batch that did not pass the QA test");
		lblNewLabel.setBounds(152, 104, 424, 15);
		blockingPanel.add(lblNewLabel);
		
		// DELIVER PANEL ////////////////////////////
		
		
		JPanel deliverPanel = new JPanel();
		tabbedPane_1.addTab("Deliver order", null, deliverPanel, null);
		deliverPanel.setLayout(null);
		
		JLabel lblEnterTheOrder = new JLabel("Enter the order number you want to deliver to the customer ");
		lblEnterTheOrder.setBounds(136, 43, 429, 15);
		deliverPanel.add(lblEnterTheOrder);
		
		deliverTextField = new JTextField();
		deliverTextField.setBounds(277, 125, 114, 19);
		deliverPanel.add(deliverTextField);
		deliverTextField.setColumns(10);
		
		JButton btnDeliver = new JButton("Deliver");
		btnDeliver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = deliverTextField.getText();
				try{
					int orderNbr = Integer.parseInt(text);
					if(orderNbr < 1) {
						JOptionPane.showMessageDialog(null, "The order number has to be a positive integer");
						return;
					}
					if(!be.orderExist(orderNbr)) {
						JOptionPane.showMessageDialog(null, "An order with that number does not exist");
						return;
					}
					//TODO: implementera den nedanför
					if(!be.movePalletToDelivered(orderNbr)) {
						JOptionPane.showMessageDialog(null, "An order with that number does not exist");
					}else {
						JOptionPane.showMessageDialog(null, "Order number " + orderNbr + " has been d");
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The order number has to be a positive integer");
				}
			}
		});
		btnDeliver.setBounds(274, 250, 117, 25);
		deliverPanel.add(btnDeliver);


		// Move PANEL ////////////////////////////
		
		
		JPanel movePanel = new JPanel();
		tabbedPane_1.addTab("Move pallet", null, movePanel, null);
		movePanel.setLayout(null);
				
		JLabel moveLabel = new JLabel("Enter the pallet number you want to move from production to storage");
		moveLabel.setBounds(136, 43, 429, 15);
		movePanel.add(moveLabel);
		
		final JTextField moveTextField = new JTextField();
		moveTextField.setBounds(277, 125, 114, 19);
		movePanel.add(moveTextField);
		moveTextField.setColumns(10);
		
		JButton btnMove = new JButton("Move pallet");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = moveTextField.getText();
				try{
					int palletNbr = Integer.parseInt(text);
					if(palletNbr < 1) {
						JOptionPane.showMessageDialog(null, "The pallet number has to be a positive integer");
						return;
					}
					if(!be.movePalletToStorage(palletNbr)) {
						JOptionPane.showMessageDialog(null, "The pallet does not exist");
						return;
					} else {
						JOptionPane.showMessageDialog(null, "The pallet has been moved to storage");
					}
					
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The pallet number has to be a positive integer");
				}
			}
		});
		btnMove.setBounds(274, 250, 117, 25);
		movePanel.add(btnMove);
		

		frame.setVisible(true);
	}

	class CookieListModel extends AbstractListModel<String> {
		private ArrayList<String> cookies = new ArrayList<String>();

		public CookieListModel(BackEnd be){
			cookies = be.getCookies();		
		}

		public int getSize() {
			return cookies.size();
		}

		public String getElementAt(int index) {
			return cookies.get(index);
		}
	}

	class BlockingListModel extends AbstractListModel<String> {
		private ArrayList<String> blocking = new ArrayList<String>();

		public BlockingListModel(BackEnd be){
			blocking = be.getBatches();
		}

		@Override
		public int getSize() {
			return blocking.size();
		}

		@Override
		public String getElementAt(int index) {
			return blocking.get(index);
		}
	}
	
	class StatusListModel extends AbstractListModel<String> {
		private ArrayList<String> statuses = new ArrayList<String>();

		public StatusListModel(BackEnd be){
			statuses = be.getStatuses();
		}

		@Override
		public int getSize() {
			return statuses.size();
		}

		@Override
		public String getElementAt(int index) {
			return statuses.get(index);
		}
	}
	
	class CustomerListModel extends AbstractListModel<String> {
		private ArrayList<String> customers = new ArrayList<String>();

		public CustomerListModel(BackEnd be){
			customers = be.getCustomers();
		}

		@Override
		public int getSize() {
			return customers.size();
		}

		@Override
		public String getElementAt(int index) {
			return customers.get(index);
		}
	}
}
