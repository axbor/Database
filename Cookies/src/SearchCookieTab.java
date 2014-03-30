import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

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


public class SearchCookieTab {
	
	public SearchCookieTab(final BackEnd be, JPanel searchCookiePanel) {
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
					JTable table = new JTable(buildCookieTableModel(cookieName));
					JOptionPane.showMessageDialog(null, new JScrollPane(table));
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}

			private TableModel buildCookieTableModel(String status) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Batch Id");
				columnNames.add("Pallet Id");
				columnNames.add("Status");
				columnNames.add("QA-result");
				Vector<Vector<String>> data = be.searchByCookie(status);
				return new DefaultTableModel(data, columnNames);
			}
		});
		btnOk_1.setBounds(172, 189, 117, 25);
		searchCookiePanel.add(btnOk_1);
	}
}
