package FinalProject;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import static FinalProject.PokeDex.PokemonIconDir;

public class PokeTableModel extends DefaultTableModel {
	Connection db;
	Vector<Integer> nationalPokeID;   
	int selectedRow;           
	
	public PokeTableModel() {
		Object[] columns = {"Pic", "Name", "Type(s)"};
		this.setColumnIdentifiers(columns);
	}
	
	public void login(String username, String password) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String connectString = "jdbc:postgresql://flowers.mines.edu/csci403";

		db = DriverManager.getConnection(connectString, username, password);
	}
	
	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
	
	public int getPokemonID() {
		if (selectedRow != -1) {
			return nationalPokeID.elementAt(selectedRow);
		}
		else {
			return -1;
		}
	}
	
	public String getPokemonName() {
		if (selectedRow != -1) {
			return (String) getValueAt(selectedRow, 1);
		}
		else {
			return "";
		}
	}
	
	// TODO
	public List<String> getPokemonType() {
		String types;
		if (selectedRow != -1) {
			types =  (String)getValueAt(selectedRow, 2);
			
			if (types.contains(",")){
				
			}
		}

		return null;
	}
	
	
	// Queries
	public void setTable(ResultSet res) throws SQLException{
		if (res == null) {
			throw new RuntimeException("Invalid search result");
		}
		this.dataVector = new Vector<Vector<Object>>();
		nationalPokeID = new Vector<>();
		while (res.next()){
			Vector<Object> row = new Vector<>();
			int id = res.getInt(1);
			
			row.add(Toolkit.getDefaultToolkit().getImage(PokemonIconDir+id+".png"));
			row.add(res.getString(1));
			row.add(null);
			
			dataVector.add(row);
			nationalPokeID.add(id);
		}
		this.fireTableDataChanged();
	}
	
	// TODO
	public void basicSearch(String val) throws SQLException {

		ResultSet result;
		
		String query =
				"SELECT ar.name, al.title, al.year, al.id " +
						"FROM artist AS ar, album AS al " +
						"WHERE lower(al.title) LIKE lower(?) " +
						"AND ar.id = al.artist_id " +
						"ORDER BY ar.name, al.year, al.title";
	}
	
}
