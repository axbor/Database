import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
	private JTextField startDate;
	private JTextField endDate;
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
		
		JList<String> cookieList = new JList<String>();
		cookieList.setModel(new CookieListModel(be));
		cookieList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane cookieScrollPane = new JScrollPane();
		cookieScrollPane.setViewportView(cookieList);
		searchCookiePanel.add(cookieScrollPane);
		cookieScrollPane.setBounds(12, 12, 148, 364);		
		
		JButton btnOk_1 = new JButton("OK");
		btnOk_1.setBounds(172, 189, 117, 25);
		searchCookiePanel.add(btnOk_1);
		
		table = new JTable();
		table.setBounds(313, 30, 327, 346);
		searchCookiePanel.add(table);
		
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
				Vector<Vector<String>> data = be.searchByStatus(status);
				System.out.println(columnNames.toString());
				System.out.println(data.toString());
				return new DefaultTableModel(data, columnNames);
			}
		});
		button_3.setBounds(172, 226, 117, 25);
		searchStatuspanel.add(button_3);
		
		//search delivery Tab /////////////////////////
		
		JPanel searchDeliverypanel = new JPanel();
		searchPane.addTab("By delivery", null, searchDeliverypanel, null);
		searchDeliverypanel.setLayout(null);
		
		JList<String> deliveryList = new JList<String>();
		deliveryList.setModel(new DeliveryListModel(be));
		deliveryList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		deliveryList.setBounds(12, 40, 148, 364);
		searchDeliverypanel.add(deliveryList);
		
		JButton button_2 = new JButton("OK");
		button_2.setBounds(172, 217, 117, 25);
		searchDeliverypanel.add(button_2);
		
		table_1 = new JTable();
		table_1.setBounds(313, 58, 327, 346);
		searchDeliverypanel.add(table_1);
		
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
		
		startDate = new JTextField();
		startDate.setBounds(66, 95, 114, 19);
		searchTimepanel.add(startDate);
		startDate.setColumns(10);
		
		endDate = new JTextField();
		endDate.setBounds(203, 95, 114, 19);
		searchTimepanel.add(endDate);
		endDate.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(405, 92, 117, 25);
		searchTimepanel.add(btnSearch);
		
		timeTable = new JTable();
		timeTable.setBounds(66, 126, 538, 278);
		searchTimepanel.add(timeTable);
		
		
		
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
		textArea_1.setBounds(55, 279, 386, 131);
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
					//TODO: implementera den nedanför
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
		
		// BLOCKING PANEL ////////////////////////////
		
		
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
					//TODO: implementera den nedanför
//					if(!be.deliverOrder(orderNbr)) {
//						JOptionPane.showMessageDialog(null, "An order with that number does not exist");
//					}else {
//						JOptionPane.showMessageDialog(null, "Order number " + orderNbr + " has been blocked");
//					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The order number has to be a positive integer");
				}
			}
		});
		btnDeliver.setBounds(274, 250, 117, 25);
		deliverPanel.add(btnDeliver);


		

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
	
	class DeliveryListModel extends AbstractListModel<String> {
		private ArrayList<String> delivery = new ArrayList<String>();

		public DeliveryListModel(BackEnd be){
			//TODO: implementera nedan
//			delivery = be.getDeliveries();
			delivery.add("skicakd");
		}

		@Override
		public int getSize() {
			return delivery.size();
		}

		@Override
		public String getElementAt(int index) {
			return delivery.get(index);
		}
	}
	
	class StatusListModel extends AbstractListModel<String> {
		private ArrayList<String> statuses = new ArrayList<String>();

		public StatusListModel(BackEnd be){
			//TODO: implementera nedan
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
}
