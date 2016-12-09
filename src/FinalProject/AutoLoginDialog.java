package FinalProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

// may want to add a splash later

public class AutoLoginDialog extends JDialog {

	private static final String passwd = "123Poke";

	PokeTableModel model;

	public AutoLoginDialog(PokeTableModel m) {
		model = m;
	}

	public void open() {
		setTitle("Login");

		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(400, 150));
		
		JLabel info = new JLabel("Now logging into Pokemon database, please wait...");

		this.add(info,BorderLayout.CENTER);

		// exit when cross is clicked
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setVisible(false);
		login();
	}

	private String partPassword(){
		String end = "on";
		 end = (char)(110-1) +end;
		 return end;
	}
	
	private void login() {
		try {
			model.login("huhwang", (passwd.substring(3)) + partPassword());
			dispose();
		}
		catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this,
					"PostgreSQL driver not found, exiting.",
					"Login error",
					ERROR_MESSAGE);
			System.exit(1);
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(this,
					"Login error: " + e.getMessage(),
					"Login error",
					ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
