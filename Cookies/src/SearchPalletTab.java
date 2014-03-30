import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class SearchPalletTab {
	
	public SearchPalletTab(final BackEnd be, JPanel searchPalletPanel) {
		final JTextField textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(125, 106, 213, 82);
		searchPalletPanel.add(textField);
		
		JLabel label = new JLabel("Write pallet number of the pallet you want to search for");
		label.setBounds(55, 68, 428, 15);
		searchPalletPanel.add(label);
		
		final JTextArea textArea_1 = new JTextArea();
		
		JButton button = new JButton("Ok");
		textArea_1.setEditable(false);
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
		searchPalletPanel.add(button);
		
		
		textArea_1.setColumns(10);
		textArea_1.setBounds(55, 279, 557, 131);
		searchPalletPanel.add(textArea_1);
		
		JLabel label_1 = new JLabel("Info");
		label_1.setBounds(56, 233, 70, 15);
		searchPalletPanel.add(label_1);
	}
}
