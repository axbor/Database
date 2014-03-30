import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class SearchTimeTab {
	
	public SearchTimeTab(final BackEnd be, JPanel searchTimePanel) {
		JLabel lblEnterTheTime = new JLabel("Enter the production time interval you want to search pallets for");
		lblEnterTheTime.setBounds(89, 25, 504, 15);
		searchTimePanel.add(lblEnterTheTime);
		
		JLabel lblNewLabel_1 = new JLabel("Start date");
		lblNewLabel_1.setBounds(89, 68, 92, 15);
		searchTimePanel.add(lblNewLabel_1);
		
		JLabel lblEndDate = new JLabel("End date");
		lblEndDate.setBounds(212, 68, 70, 15);
		searchTimePanel.add(lblEndDate);
		
		final JTextField startDateField = new JTextField();
		startDateField.setBounds(66, 95, 114, 19);
		searchTimePanel.add(startDateField);
		startDateField.setColumns(10);
		
		final JTextField endDateField = new JTextField();
		endDateField.setBounds(203, 95, 114, 19);
		searchTimePanel.add(endDateField);
		endDateField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String startDateString = startDateField.getText();
				String endDateString = endDateField.getText(); 
				try{
					java.sql.Date startDate = java.sql.Date.valueOf(startDateString);
					java.sql.Date endDate = java.sql.Date.valueOf(endDateString);
					JTable timeTable= new JTable(buildTimeTableModel(startDate, endDate));
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
		searchTimePanel.add(btnSearch);
	}
}
