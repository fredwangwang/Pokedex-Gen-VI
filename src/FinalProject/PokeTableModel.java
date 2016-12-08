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
		
	}

	public int getSelectedPokemonID() {
		if (selectedRow != -1) {
			if (nationalPokeID.size() > SelectedPokemonID)
				SelectedPokemonID =  nationalPokeID.elementAt(selectedRow);
		}
		else {
			SelectedPokemonID =  0;
		};
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

	public String getSelectedPokemoneggGroup(int id) throws SQLException {
		String egggroup = "";
		ResultSet result;

		String query = 
				"SELECT e.identifier "
				+ "FROM egg_groups AS e, pokemon_species AS ps, pokemon_egg_groups AS peg "
				+ "WHERE ps.id = (?) AND ps.id = peg.species_id AND peg.egg_group_id = e.id";
		
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, id); //What does this do? It stop throwing an error and works after I commented it out - Khanh
						// This is a prepared statement substitution. Followed the style of CPW. - Huan
		result =  ps.executeQuery();

		result.next();
		egggroup = CommonUtils.capitalize(result.getString(1));

		while (result.next()){
			egggroup += ", " + CommonUtils.capitalize(result.getString(1));
		}

		return egggroup;
	}

	public String getSelectedPokemonCaptureRate(int id) throws SQLException{
		String captureRate = "";
		ResultSet result;
		String query = 
				"SELECT capture_rate FROM pokemon_species "
				+ "WHERE id = " + id;
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		captureRate = result.getString(1);

		return captureRate;
	}

	public String getSelectedPokemonGenderRatio(int id) throws SQLException{
		ResultSet result;
		String query = 
				"SELECT gender_rate FROM pokemon_species "
				+ "WHERE id = " + id;
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		double female = Double.parseDouble(result.getString(1));
		if ((int)female == -1) { 
			return "genderless";
		}
		female *= 12.5;
		double male = 100.0 - female;
		return male + "% male, " + female + "% female";
	}

	public String getSelectedPokemonBaseExp(int id) throws SQLException{
		// query returns int, make it a string to return (pokemonspecies)
		String base = "";
		ResultSet result;
		String query = 
				"SELECT base_experience FROM pokemon "
				+ "WHERE id = " + id;
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		base = result.getString(1);

		return base;
	}

	public String getSelectedPokemonBaseHappness(int id) throws SQLException{
		String baseHappiness = "";
		ResultSet result;
		String query = 
				"SELECT base_happiness FROM pokemon_species "
				+ "WHERE id = " + id;
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		baseHappiness = result.getString(1);

		return baseHappiness;
	}

	public String getSelectedPokemonGeneration(int id) throws SQLException{
		// Might work, might not, who knows
		String generation;
		ResultSet result;
		String query = 
				"SELECT g.identifier "
				+ "FROM pokemon_types AS pt, types AS t "
				+ "WHERE pt.pokemon_id = " + id + " AND g.id = ps.generation_id";
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		generation = result.getString(1);
		
		return generation;
	}

	public String getSelectedPokemonRegion(int id) throws SQLException{
		//NOTE: This will return the region id, name, and generation
		// will need to be changed if that is not what you want
		String gen;
		String region;
		ResultSet result;
		String query1 = 
				"SELECT g.identifier"
				+ "FROM generations AS g, pokemon_species AS ps"
				+ "WHERE ps.id = " + id + " AND g.id = ps.generation_id";
		
		PreparedStatement ps1 = db.prepareStatement(query1);
		result =  ps1.executeQuery();
		result.next();
		gen = result.getString(1);
		
		String query2 = 
				"SELECT p.generation_id, r.identifier"
				+ "FROM pokemon_species as p, regions as r"
				+ "WHERE p.id= " + id + " AND p.generation_id=r.id";
		
		PreparedStatement ps2 = db.prepareStatement(query2);
		result =  ps2.executeQuery();
		result.next();
		region = result.getString(1);
		
		return region + gen;
	}
	
	public Vector<Integer> getSelectedPokemonStatus(int id) throws SQLException{
		// TODO do query
		
		//  (modify query 2)
		Vector<Integer> stats= new Vector<Integer>();
		ResultSet result;
		String query = 
				"SELECT ps.base_stat"
				+ "FROM pokemon_stats AS ps, stats AS s"
				+ "WHERE ps.pokemon_id = " + id + " AND s.id = ps.stat_id";
		
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();
		result.next();
		while(result.next()) {
			stats.add(result.getInt(1));
		}
/*		stats.add(100);
		stats.add(100);
		stats.add(100);
		stats.add(100);
		stats.add(100);
		stats.add(100);*/
		return stats;
	}
	
	// add query 5 here.
	// add query 6 here.
	// add query 10 here
	// add query 11 here
	// add query 12 here.
}
