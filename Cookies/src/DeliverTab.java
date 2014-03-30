import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DeliverTab {
	
	public DeliverTab(final BackEnd be, JPanel deliverPanel) {
		JLabel lblEnterTheOrder = new JLabel("Enter the order number you want to deliver to the customer ");
		lblEnterTheOrder.setBounds(136, 43, 429, 15);
		deliverPanel.add(lblEnterTheOrder);
		
		final JTextField deliverTextField = new JTextField();
		deliverTextField.setBounds(277, 125, 114, 19);
		deliverPanel.add(deliverTextField);
		deliverTextField.setColumns(10);
		
		JButton btnDeliver = new JButton("Deliver");
		btnDeliver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = deliverTextField.getText();
				try{
					int orderNbr = Integer.parseInt(text);
					if(orderNbr < 1) {
						JOptionPane.showMessageDialog(null, "The order number has to be a positive integer");
						return;
					}
					if(!be.orderExist(orderNbr)) {
						JOptionPane.showMessageDialog(null, "An order with that number does not exist");
						return;
					}
					//TODO: implementera den nedanför
					if(be.movePalletToDelivered(orderNbr).isEmpty()) {
						JOptionPane.showMessageDialog(null, "An order with that number has already been delivered");
					}else {
						JOptionPane.showMessageDialog(null, "Order number " + orderNbr + " has been delivered");
					}
				}catch(NumberFormatException err) {
					JOptionPane.showMessageDialog(null, "The order number has to be a positive integer");
				}
			}
		});
		btnDeliver.setBounds(274, 250, 117, 25);
		deliverPanel.add(btnDeliver);
	}
}
