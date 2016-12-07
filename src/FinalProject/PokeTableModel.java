package FinalProject;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import static FinalProject.PokeDex.PokemonIconDir;

public class PokeTableModel extends DefaultTableModel {
	// override to show pictures
	@Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0: return ImageIcon.class;
            default: return String.class;
        }
    }
	
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
			
			row.add(new ImageIcon(PokemonIconDir+id+".png"));
			row.add(res.getString(2));
			row.add(res.getString(3));
			this.addRow(row);
			nationalPokeID.add(id);
		}
		this.fireTableDataChanged();
	}
	
	// TODO
	public void basicSearch(String val) throws SQLException {

		ResultSet result;
		
		String query =
				"SELECT pkids.id, pspecies.identifier, types.identifier " +
				 "FROM (SELECT id FROM pokemon_species WHERE identifier LIKE (?)) AS pkids, " +
								"pokemon_species AS pspecies, " +
								"pokemon_types AS ptype, " +
								"types " +
				"WHERE pkids.id = pspecies.id AND pkids.id = ptype.pokemon_id AND ptype.type_id = types.id";
		
		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, "%" + val.toLowerCase() + "%");
		
		result =  ps.executeQuery();
		setTable(result);
	}
	
}
