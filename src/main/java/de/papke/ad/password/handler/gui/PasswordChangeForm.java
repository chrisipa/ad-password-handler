package de.papke.ad.password.handler.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import de.papke.ad.password.handler.PasswordChangeHandler;
import de.papke.ad.password.handler.PasswordExpirationHandler;

public class PasswordChangeForm {
	
	private JPasswordField oldPasswordInput;
	private JPasswordField newPasswordInput;
	private JPasswordField newPasswordRepeatInput;
	
	private PasswordExpirationHandler passwordExpirationHandler;
	private PasswordChangeHandler passwordChangeHandler;

	public PasswordChangeForm(PasswordExpirationHandler passwordExpirationHandler, PasswordChangeHandler passwordChangeHandler) {
		this.passwordExpirationHandler = passwordExpirationHandler;
		this.passwordChangeHandler = passwordChangeHandler;
	}

	public void init(){
		
		// Basic form create
		int days = passwordExpirationHandler.getDaysTillPasswordExpires();
		JFrame frame = new JFrame("Password expiration");
		frame.setLocationRelativeTo(null);
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creating the grid
		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();

		// Create some elements
		int gridWithOrig = c.gridwidth;
		int ipadyOrig = c.ipady;
		JLabel introText = new JLabel("Your password will expire in " + days + " days.");
		c.gridwidth = 2;
		c.ipady = 25;
		panel.add(introText, c);
		c.gridwidth = gridWithOrig;
		c.ipady = ipadyOrig;
		
		JLabel oldPasswordLabel = new JLabel("Old password");
		c.gridx = 0;
		c.gridy = 1;
		panel.add(oldPasswordLabel,c);
		
		oldPasswordInput = new JPasswordField(10);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(oldPasswordInput,c);

		JLabel newPasswordLabel = new JLabel("New password");
		c.gridx = 0;
		c.gridy = 2;
		panel.add(newPasswordLabel,c);
		
		newPasswordInput = new JPasswordField(10);
		c.gridx = 1;
		c.gridy = 2;
		panel.add(newPasswordInput,c);
		
		JLabel newPasswordRepeatLabel = new JLabel("New password");
		c.gridx = 0;
		c.gridy = 3;
		panel.add(newPasswordRepeatLabel,c);
		
		newPasswordRepeatInput = new JPasswordField(10);
		c.gridx = 1;
		c.gridy = 3;
		panel.add(newPasswordRepeatInput,c);

		JButton changeInput = new JButton("Change");
		c.gridx = 0;
		c.gridy = 4;
		changeInput.addActionListener(new ChangePasswordButtonListener());
		panel.add(changeInput,c);

		frame.setVisible(true);
	}

	class ChangePasswordButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			if (String.valueOf(newPasswordInput.getPassword()).equals(String.valueOf(newPasswordRepeatInput.getPassword()))) {
				
				boolean success = passwordChangeHandler.changePassword(String.valueOf(newPasswordInput.getPassword()));
				
				if (success) {
					JOptionPane.showMessageDialog(null, "You have successfully changed your password");	
				}
				else {
					JOptionPane.showMessageDialog(null, "An error occured while changing your password");	
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "You have entered two different new passwords");	
			}
		}
	}
}