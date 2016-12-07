package FinalProject;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	int SelectedPokemonID;

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
		if (selectedRow != -1) {
			if (nationalPokeID.size() > SelectedPokemonID)
				SelectedPokemonID =  nationalPokeID.elementAt(selectedRow);
		}
		else {
			SelectedPokemonID =  0;
		}
	}

	public int getSelectedPokemonID() {
		return SelectedPokemonID;
	}

	public String getSelectedPokemonName() {
		return (String) getValueAt(selectedRow, 1);
	}

	public String getSelectedPokemonType() {
		return (String) getValueAt(selectedRow, 2);
	}

	// Queries
	public void setTable(ResultSet res) throws SQLException{
		int id = -1, lastid = -1;

		Vector<Object> row;

		if (res == null) {
			throw new RuntimeException("Invalid search result");
		}
		this.dataVector = new Vector<Vector<Object>>();
		nationalPokeID = new Vector<>();
		while (res.next()){
			id = res.getInt(1);
			// Means same pokemon with different type
			if (id != lastid){
				row = new Vector<>();
				row.add(new ImageIcon(PokemonIconDir+id+".png"));
				row.add(CommonUtils.capitalize(res.getString(2)));
				row.add(CommonUtils.capitalize(res.getString(3)));
				this.addRow(row);
				nationalPokeID.add(id);
				lastid = id;
			}
			else {
				String types = (String) this.getValueAt(this.getRowCount() - 1, 2) + ", " + CommonUtils.capitalize(res.getString(3));
				this.setValueAt(types, this.getRowCount() - 1 , 2);
			}
		}
		this.fireTableDataChanged();
	}

	public void nameSearch(String val) throws SQLException {
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

	public Vector<Vector> getPokemonStatus() throws SQLException {
		Vector<String> typeNames = new Vector<>();
		Vector<Integer> typeInts = new Vector<>();

		String query =
				"SELECT id,  identifier " +
						"FROM stats " +
						"WHERE is_battle_only = 0";

		Statement stmt = db.createStatement();
		ResultSet result = stmt.executeQuery(query);
		while(result.next()){
			typeInts.add(result.getInt(1));
			typeNames.add(CommonUtils.capitalize(result.getString(2)));			
		}

		Vector<Vector> type = new Vector<>();
		type.add(typeInts);
		type.add(typeNames);

		return type;
	}

	// TODO 
	public String getSelectedPokemoneggGroup(int id) throws SQLException {
		String egggroup = "";
		ResultSet result;
		String query =
				"";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, id);
		result =  ps.executeQuery();

		result.next();
		egggroup = result.getString(1);

		while (result.next()){
			egggroup += ", " +result.getString(1);
		}
		
		return egggroup;
	}

	public String getSelectedPokemonCaptureRate(int id){
		// TODO do query
		// query returns int, make it a string to return (pokemonspecies)

		return "";
	}

	public String getSelectedPokemonGenderRatio(int id){
		// TODO do query
		// query returns int, make it a string to return (pokemonspecies)
		return "";
	}

	public String getSelectedPokemonBaseExp(int id){
		// TODO do query
		// query returns int, make it a string to return (pokemonspecies)
		return "";
	}

	public String getSelectedPokemonBaseHappness(int id){
		// TODO do query
		// query returns int, make it a string to return (pokemonspecies)
		return "";
	}
}
