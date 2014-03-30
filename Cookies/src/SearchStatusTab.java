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


public class SearchStatusTab {
	
	public SearchStatusTab(final BackEnd be, JPanel searchStatusPanel) {
		final JList<String> statusList = new JList<String>();
		statusList.setModel(new StatusListModel(be));
		statusList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		statusList.setBounds(12, 49, 148, 364);
		searchStatusPanel.add(statusList);

		JButton button_3 = new JButton("Search");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int index = statusList.getSelectedIndex();
					ListModel<String> model = statusList.getModel();
					String status = model.getElementAt(index);
					JTable table_2 = new JTable(buildStatusTableModel(status));
					JOptionPane.showMessageDialog(null, new JScrollPane(table_2));
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "Amount has to be an integer bigger than 0");
				}
			}

			private TableModel buildStatusTableModel(String status) {
				Vector<String> columnNames = new Vector<String>();
				columnNames.add("Batch Id");
				columnNames.add("Pallet Id");
				columnNames.add("Cookie");
				columnNames.add("QA-result");
				Vector<Vector<String>> data = be.searchByStatus(status);
				return new DefaultTableModel(data, columnNames);
			}
		});
		button_3.setBounds(172, 226, 117, 25);
		searchStatusPanel.add(button_3);
	}
}
