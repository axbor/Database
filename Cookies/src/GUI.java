import java.awt.*;

import javax.swing.*;

/**
 * GUI is the user interface to the database. It sets up the main
 * window.
 */
public class GUI {

	/**
	 * Create a GUI object and connect to the database.
	 * 
	 * @param be
	 *            The database.
	 * @wbp.parser.entryPoint
	 */
	public GUI(BackEnd b) {
		final BackEnd be = b;
		JFrame frame = new JFrame("Cookies");	
		frame.setSize(700,500);
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane_1, BorderLayout.CENTER);

		//create Tab /////////////////////////
		JPanel createPanel = new JPanel();
		tabbedPane_1.addTab("Create pallet", null, createPanel, null);
		createPanel.setLayout(null);
		new CreateTab(be, createPanel);
		
		//search Tab /////////////////////////
		JTabbedPane searchPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.addTab("Search", null, searchPane, null);
		
		//search cookie Tab /////////////////////////
		JPanel searchCookiePanel = new JPanel();
		searchPane.addTab("By cookie", null, searchCookiePanel, null);
		searchCookiePanel.setLayout(null);
		new SearchCookieTab(be, searchCookiePanel);
		
		//search status Tab /////////////////////////
		JPanel searchStatusPanel = new JPanel();
		searchPane.addTab("By status", null, searchStatusPanel, null);
		searchStatusPanel.setLayout(null);
		new SearchStatusTab(be, searchStatusPanel);
		
		//search time Tab /////////////////////////
		JPanel searchTimePanel = new JPanel();
		searchPane.addTab("By time", null, searchTimePanel, null);
		searchTimePanel.setLayout(null);
		new SearchTimeTab(be, searchTimePanel);
		
		//search pallet Tab /////////////////////////
		JPanel searchPalletPanel = new JPanel();
		searchPalletPanel.setLayout(null);
		searchPane.addTab("By pallet number", null, searchPalletPanel, null);
		new SearchPalletTab(be, searchPalletPanel);
		
		//search batch Tab /////////////////////////
		JPanel searchBatchPanel = new JPanel();
		searchBatchPanel.setLayout(null);
		searchPane.addTab("By batch number", null, searchBatchPanel, null);
		new SearchBatchTab(be, searchBatchPanel);

		//search Customer Tab /////////////////////////
		JPanel searchCustomerPanel = new JPanel();
		searchCustomerPanel.setLayout(null);
		searchPane.addTab("By customer", null, searchCustomerPanel, null);
		new SearchCustomerTab(be, searchCustomerPanel);

		//blocking panel /////////////////////////////////
		JPanel qaPanel = new JPanel();
		tabbedPane_1.addTab("QA-test", null, qaPanel, null);
		qaPanel.setLayout(null);
		new QATab(be, qaPanel);
		
		// DELIVER PANEL ////////////////////////////
		JPanel deliverPanel = new JPanel();
		tabbedPane_1.addTab("Deliver order", null, deliverPanel, null);
		deliverPanel.setLayout(null);
		new DeliverTab(be, deliverPanel);

		// Move PANEL ////////////////////////////
		JPanel movePanel = new JPanel();
		tabbedPane_1.addTab("Move pallet to storage", null, movePanel, null);
		movePanel.setLayout(null);
		new MoveTab(be, movePanel);
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
