package FinalProject;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.Component;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class CommonUtils {

	public static String capitalize(String str){
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	public static void sqlExceptionHandler(SQLException e, Component parentComponent) {
		JOptionPane.showMessageDialog(parentComponent,
				"Database error: " + e.getMessage(),
				"Database error",
				ERROR_MESSAGE);
	}
}
