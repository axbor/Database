import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class SearchBatchTab {
	
	public SearchBatchTab(final BackEnd be, JPanel searchBatchPanel) {
		final JTextField textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(125, 106, 213, 82);
		searchBatchPanel.add(textField_2);
		
		JLabel label_2 = new JLabel("Write batch number of the batch you want to search for");
		label_2.setBounds(55, 68, 428, 15);
		searchBatchPanel.add(label_2);
		
		final JTextArea textArea_3 = new JTextArea();
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
		searchBatchPanel.add(button_1);
		
		textArea_3.setEditable(false);
		textArea_3.setColumns(10);
		textArea_3.setBounds(55, 279, 608, 131);
		searchBatchPanel.add(textArea_3);
		
		JLabel label_3 = new JLabel("Info");
		label_3.setBounds(56, 233, 70, 15);
		searchBatchPanel.add(label_3);
	}
}
