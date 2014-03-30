import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class CreateTab {
	/**
	 * tabbedPane is the contents of the window. It consists of two panes, User
	 * login and Book tickets.
	 */
	public JTextField cookieAmountField;
	public int amount;
	public JTextField textField;
	public JTextField textField_2;
	public JTextArea textArea_1;
	public JTextArea textArea_3;
	public JTable table;
	public JTable table_1;
	public JTable table_2;
	public JTextField startDateField;
	public JTextField endDateField;
	public JTable timeTable;
	public JTextField deliverTextField;

	public CreateTab(final BackEnd be, JPanel createPanel) {
		final JList<String> list = new JList<String>();
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

		JButton btnOk = new JButton("Create batch");
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
	}
}