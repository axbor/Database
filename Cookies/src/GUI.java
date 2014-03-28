import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.stream.Location;

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
	private JTextField palletNumberField;
	private JTextField palletInfoField;
	private JTextField batchNumberField;
	private JTextField batchInfoField;

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
		frame.setSize(500,500);

		//create Tab /////////////////////////

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane_1, BorderLayout.CENTER);

		JPanel createPanel = new JPanel();
		tabbedPane_1.addTab("Create", null, createPanel, null);
		createPanel.setLayout(null);

		list = new JList<String>();
		list.setModel(new CookieListModel(be));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		createPanel.add(list);
		list.setBounds(50, 94, 150, 200);

		cookieAmountField = new JTextField();
		cookieAmountField.setBounds(284, 106, 144, 62);
		createPanel.add(cookieAmountField);
		cookieAmountField.setColumns(10);

		JButton btnOk = new JButton("OK");
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
						cookieAmountField.setText(cookie);
						//TODO: implementera den nedanför
						be.createPallet(cookie, amount);
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}
		});
		btnOk.setBounds(284, 291, 117, 25);
		createPanel.add(btnOk);

		JLabel lblAmountOfPallets = new JLabel("Amount of Pallets");
		lblAmountOfPallets.setBounds(284, 79, 136, 15);
		createPanel.add(lblAmountOfPallets);

		JLabel lblChooseACookie = new JLabel("Choose a cookie type to create");
		lblChooseACookie.setBounds(33, 51, 221, 15);
		createPanel.add(lblChooseACookie);

		//Search Pallet tab////////////////////////////

		JPanel searchPalletPanel = new JPanel();
		tabbedPane_1.addTab("Search Pallet", null, searchPalletPanel, null);
		searchPalletPanel.setLayout(null);

		palletNumberField = new JTextField();
		palletNumberField.setBounds(125, 106, 213, 82);
		searchPalletPanel.add(palletNumberField);
		palletNumberField.setColumns(10);

		JLabel lblWritePalletNumber = new JLabel("Write pallet number of the pallet you want to search for");
		lblWritePalletNumber.setBounds(55, 68, 428, 15);
		searchPalletPanel.add(lblWritePalletNumber);

		JButton btnOk_1 = new JButton("Ok");
		btnOk_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pallet = palletNumberField.getText();
				try{
					int palletNbr = Integer.parseInt(pallet);
					if(palletNbr < 1) {
						JOptionPane.showMessageDialog(null, "The pallet number has to be bigger than 0");
					}else {
						//TODO: implementera den nadanför
						String info = be.getPalletInfo(palletNbr);
						//						palletInfoField.setText(info);
						palletInfoField.setText("hej");
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The pallet number is an integer bigger than 0");
				}
			}
		});
		btnOk_1.setBounds(172, 200, 117, 25);
		searchPalletPanel.add(btnOk_1);

		palletInfoField = new JTextField();
		palletInfoField.setEditable(false);
		palletInfoField.setBounds(55, 279, 386, 131);
		searchPalletPanel.add(palletInfoField);
		palletInfoField.setColumns(10);

		//Hämta info från pallen


		JLabel lblInfo = new JLabel("Info");
		lblInfo.setBounds(56, 233, 70, 15);
		searchPalletPanel.add(lblInfo);



		//Search Batch tab////////////////////////////

		JPanel searchBatchPanel = new JPanel();
		tabbedPane_1.addTab("Search Batch", null, searchBatchPanel, null);
		searchBatchPanel.setLayout(null);

		batchNumberField = new JTextField();
		batchNumberField.setBounds(125, 106, 213, 82);
		searchBatchPanel.add(batchNumberField);
		batchNumberField.setColumns(10);

		JLabel lblWriteBatchNumber = new JLabel("Write batch number of the batch you want to search for");
		lblWriteBatchNumber.setBounds(55, 68, 428, 15);
		searchBatchPanel.add(lblWriteBatchNumber);

		JButton btnOk_2 = new JButton("Ok");
		btnOk_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String batch = palletNumberField.getText();
				try{
					int batchNbr = Integer.parseInt(batch);
					if(batchNbr < 1) {
						JOptionPane.showMessageDialog(null, "The batch number has to be bigger than 0");
					}else {
						//TODO: implementera den nadanför
						String info = be.getBatchInfo(batchNbr);
						palletInfoField.setText(info);
						//						palletInfoField.setText("hej");
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The batch number is an integer bigger than 0");
				}
			}
		});
		btnOk_2.setBounds(172, 200, 117, 25);
		searchBatchPanel.add(btnOk_2);

		batchInfoField = new JTextField();
		batchInfoField.setEditable(false);
		batchInfoField.setBounds(55, 279, 386, 131);
		searchBatchPanel.add(batchInfoField);
		batchInfoField.setColumns(10);

		//Hämta info från batchen


		JLabel lblBatchInfo = new JLabel("Info");
		lblBatchInfo.setBounds(56, 233, 70, 15);
		searchBatchPanel.add(lblBatchInfo);




		//blocking panel /////////////////////////////////


		JPanel blockingPanel = new JPanel();
		tabbedPane_1.addTab("Blocking", null, blockingPanel, null);
		blockingPanel.setLayout(null);

		final JList blockList = new JList();
		blockList.setBounds(45, 47, 256, 385);
		blockList.setModel(new BlockingListModel(be));
		blockList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		blockingPanel.add(blockList);

		JButton btnBlock = new JButton("Block");
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int index = blockList.getSelectedIndex();
					ListModel<String> model = blockList.getModel();
					int batchNbr= Integer.parseInt(model.getElementAt(index));
					//TODO: implementera den nadanför
					be.blockBatch(batchNbr);
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The batch number is an integer bigger than 0");
				}
			}
		});
		btnBlock.setBounds(398, 228, 71, 25);
		blockingPanel.add(btnBlock);



		JLabel lblNewLabel = new JLabel("Block Batch");
		lblNewLabel.setBounds(45, 12, 101, 15);
		blockingPanel.add(lblNewLabel);



		frame.setVisible(true);
	}

	class CookieListModel extends AbstractListModel<String> {
		private ArrayList<String> cookies = new ArrayList<String>();

		public CookieListModel(BackEnd be){
			cookies = be.getCookies();
//			cookies.add("banankaka");
//			cookies.add("sockarkaka");
//			cookies.add("pepparkaka");
		}

		@Override
		public int getSize() {
			return cookies.size();
		}

		@Override
		public String getElementAt(int index) {
			return cookies.get(index);
		}
	}

	class BlockingListModel extends AbstractListModel<String> {
		private ArrayList<String> blocking = new ArrayList<String>();

		public BlockingListModel(BackEnd be){
			blocking = be.getBatches();
//			blocking.add("1");
//			blocking.add("2");
//			blocking.add("3");
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
}
