import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class QATab {
	
	public QATab(final BackEnd be, JPanel blockingPanel) {
		final JTextField blockTextField = new JTextField();
		blockTextField.setBounds(259, 131, 156, 85);
		blockingPanel.add(blockTextField);
		blockTextField.setColumns(10);

		JButton btnBlock = new JButton("Failed");
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = blockTextField.getText();
				try{
					int number = Integer.parseInt(text);
					if(number < 1) {
						JOptionPane.showMessageDialog(null, "The batch number has to be a positive integer");
						return;
					}
					ArrayList<Integer> blockedPallets = be.blockBatch(number);
					
					if(blockedPallets.isEmpty()){
						JOptionPane.showMessageDialog(null, "A batch with that number does not exist or it has allready been tested");
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
		btnBlock.setBounds(200, 259, 136, 25);
		blockingPanel.add(btnBlock);
		
		JButton btnPass = new JButton("Passed");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = blockTextField.getText();
				try{
					int number = Integer.parseInt(text);
					if(number < 1) {
						JOptionPane.showMessageDialog(null, "The batch number has to be a positive integer");
						return;
					}
					ArrayList<Integer> passedPallets = be.passBatch(number);
					
					if(passedPallets.isEmpty()){
						JOptionPane.showMessageDialog(null, "A batch with that number does not exist or it has allready been tested");
					}else {
						StringBuilder sb = new StringBuilder();
						sb.append("\n");
						for(int nbr : passedPallets){
							sb.append(nbr);
							sb.append("\n");
						}
						String passedStr = sb.toString();
						JOptionPane.showMessageDialog(null, "The following pallets passed the test: " + passedStr);
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The batch number has to be a positive integer");
				}
			}
		});
		btnPass.setBounds(350, 259, 136, 25);
		blockingPanel.add(btnPass);


		JTextArea lblNewLabel = new JTextArea("Enter number of the batch that has been tested \n and click on the button that represent the resulet");
		lblNewLabel.setBounds(152, 70, 350, 40);
		blockingPanel.add(lblNewLabel);
	}
}
