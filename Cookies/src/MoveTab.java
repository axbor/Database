import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MoveTab {
	
	public MoveTab(final BackEnd be, JPanel movePanel) {
		JLabel moveLabel = new JLabel("Enter the pallet number you want to move from production to storage");
		moveLabel.setBounds(100, 43, 500, 15);
		movePanel.add(moveLabel);
		
		final JTextField moveTextField = new JTextField();
		moveTextField.setBounds(277, 125, 114, 19);
		movePanel.add(moveTextField);
		moveTextField.setColumns(10);
		
		JButton btnMove = new JButton("Move pallet");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = moveTextField.getText();
				try{
					int palletNbr = Integer.parseInt(text);
					if(palletNbr < 1) {
						JOptionPane.showMessageDialog(null, "The pallet number has to be a positive integer");
						return;
					}
					boolean inProd = be.palletInProd(palletNbr); 
					boolean moved = be.movePalletToStorage(palletNbr);
					if(inProd && moved) {
						JOptionPane.showMessageDialog(null, "The palllet has been moved to storage");
					} else {
						JOptionPane.showMessageDialog(null, "The pallet does not exist");
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The pallet number has to be a positive integer");
				}
			}
		});
		btnMove.setBounds(274, 250, 117, 25);
		movePanel.add(btnMove);
	}
}
