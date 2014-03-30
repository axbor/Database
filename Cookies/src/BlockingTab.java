import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BlockingTab {
	
	public BlockingTab(final BackEnd be, JPanel blockingPanel) {
		final JTextField blockTextField = new JTextField();
		blockTextField.setBounds(259, 131, 156, 85);
		blockingPanel.add(blockTextField);
		blockTextField.setColumns(10);

		JButton btnBlock = new JButton("Block batch");
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
					
					if(blockedPallets == null){
						JOptionPane.showMessageDialog(null, "A batch with that number does not exist");
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
		btnBlock.setBounds(271, 259, 136, 25);
		blockingPanel.add(btnBlock);


		JLabel lblNewLabel = new JLabel("Enter number of the batch that did not pass the QA test");
		lblNewLabel.setBounds(152, 104, 424, 15);
		blockingPanel.add(lblNewLabel);
	}
}
