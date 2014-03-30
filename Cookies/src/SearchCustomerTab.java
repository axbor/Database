import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class SearchCustomerTab {
	
	public SearchCustomerTab(final BackEnd be, JPanel searchCustomerPanel) {
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
				columnNames.add("Order number");
				columnNames.add("Pallet Id");
				columnNames.add("Delivery time");
				Vector<Vector<String>> data = be.searchByCustomer(customer);
				return new DefaultTableModel(data, columnNames);
			}
		});
		searchCustomerBtn.setBounds(172, 226, 117, 25);
		searchCustomerPanel.add(searchCustomerBtn);

	}
	
	class CustomerListModel extends AbstractListModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
