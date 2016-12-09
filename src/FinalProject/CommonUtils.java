package FinalProject;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CommonUtils {
	public static int fieldNumber;

	public static String capitalize(String str){
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static void sqlExceptionHandler(SQLException e, Component parentComponent) {
		JOptionPane.showMessageDialog(parentComponent,
				"Database error: " + e.getMessage(),
				"Database error",
				ERROR_MESSAGE);
	}

	public static boolean isfieldReturnNumber(JTextField field, Component parentComponent){
		String s = field.getText().trim();
		if (s.length() == 0)
			return false;
		try {
			fieldNumber = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			if (parentComponent != null)
				JOptionPane.showMessageDialog(parentComponent,
						"Prase int error: " + e.getMessage(),
						"Prase Int Error",
						ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	
}
