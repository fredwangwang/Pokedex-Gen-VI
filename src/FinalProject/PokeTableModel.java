package FinalProject;

import java.sql.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import static FinalProject.PokeDex.PokemonIconDir;

public class PokeTableModel extends DefaultTableModel {
	// override to show pictures
	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0: return Integer.class;
		case 1: return ImageIcon.class;
		default: return String.class;
		}
	}	

	private Connection db;
	private int selectedRow;     
	private int SelectedPokemonID;
	private Object[] columns = {"ID", "Pic", "Name", "Type(s)"};

	public PokeTableModel() {
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
		if (selectedRow != -1) 
			SelectedPokemonID =  (int) this.getValueAt(selectedRow, 0);
		else 
			SelectedPokemonID =  0;
		return SelectedPokemonID;
	}

	public String getSelectedPokemonName() {
		return (String) getValueAt(selectedRow, 2);
	}

	public String getSelectedPokemonType() {
		return (String) getValueAt(selectedRow, 3);
	}

	// Queries
	public void setTable(Vector<Object[]> rows){
		this.dataVector = new Vector<Vector<Object>>();

		for (Object[] row : rows)
			this.addRow(row);
		fireTableDataChanged();
	}

	public void nameSearch(String val) throws SQLException {
		ResultSet res;
		String query =
				"SELECT pkids.id, pspecies.identifier, types.identifier " +
						"FROM (SELECT id FROM pokemon_species WHERE identifier LIKE (?)) AS pkids, " +
						"pokemon_species AS pspecies, " +
						"pokemon_types AS ptype, " +
						"types " +
						"WHERE pkids.id = pspecies.id AND pkids.id = ptype.pokemon_id AND ptype.type_id = types.id";

		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, "%" + val.toLowerCase() + "%");
		res =  ps.executeQuery();

		setTable(formatPokemonResult(res));
	}

	public Vector<Vector> getPokemonstats() throws SQLException {
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

	public Vector<Vector> getPokemonTypes() throws SQLException {
		Vector<String> typeNames = new Vector<>();
		Vector<Integer> typeInts = new Vector<>();

		String query =
				"SELECT id,  identifier " +
						"FROM types " +
						"WHERE id < 1000";

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

	public String getSelectedPokemoneggGroup() throws SQLException {
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonCaptureRate() throws SQLException{
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonGenderRatio() throws SQLException{
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonBaseExp() throws SQLException{
		// query returns int, make it a string to return (pokemonspecies)
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonBaseHappness() throws SQLException{
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonGeneration() throws SQLException{
		// Might work, might not, who knows
		int id = getSelectedPokemonID();
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

	public String getSelectedPokemonRegion() throws SQLException{
		//NOTE: This will return the region id, name, and generation
		// will need to be changed if that is not what you want
		int id = getSelectedPokemonID();
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

	public Vector<Integer> getSelectedPokemonstats() throws SQLException{
		int id = getSelectedPokemonID();
		Vector<Integer> stats= new Vector<Integer>();
		ResultSet result;
		String query = 
				"SELECT ps.base_stat FROM pokemon_stats AS ps, stats AS s WHERE ps.pokemon_id = " + id + " AND s.id = ps.stat_id";

		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		while(result.next()) {
			stats.add(result.getInt(1));
		}
		return stats;
	}

	public Vector<Object[]> getSelectedPokemonEvolvChain() throws SQLException {
		int Pid = getSelectedPokemonID();
		ResultSet result;
		String query = 
				"SELECT ps.id, ps.identifier, t.identifier "
						+ "FROM  pokemon_species AS ps, pokemon_types AS pt, types AS t "
						+ "WHERE evolution_chain_id = (SELECT evolution_chain_id "
						+ "FROM pokemon_species "
						+ "WHERE id="+ Pid + ") "
						+ "AND ps.id = pt.pokemon_id AND pt.type_id = t.id";

		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		return formatPokemonResult(result);
	}

	public Vector<Object[]> getQualifiedPokemonBasedStatus(int statID, int base) throws SQLException {
		ResultSet result;
		String query = 
				"SELECT distinct ps.pokemon_id, p.identifier, t.identifier " 
						+ "FROM pokemon_stats AS ps, stats AS s, pokemon_species AS p, pokemon_types AS pt, types AS t "
						+ "WHERE s.id = " + statID + " AND ps.stat_id = s.id AND ps.base_stat >= " + base +" AND ps.pokemon_id = p.id AND pt.pokemon_id = p.id AND pt.type_id = t.id "
						+ "ORDER by ps.pokemon_id";

		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		return formatPokemonResult(result);
	}

	public Vector<Object[]> getQualifiedPokemonStatusSum(int sum) throws SQLException {

		ResultSet result;
		String query = 
				"SELECT p.pokemon_id, ps.identifier, t.identifier " 
						+ "FROM (select pokemon_id, sum(base_stat) " 
						+ "FROM pokemon_stats " 
						+ "GROUP BY pokemon_id " 
						+ "ORDER BY pokemon_id asc) AS p, "
						+ "pokemon_species AS ps, "
						+ "types AS t, "
						+ "pokemon_types AS pt "
						+ "WHERE sum >= " + sum + " AND ps.id = p.pokemon_id AND pt.pokemon_id = p.pokemon_id AND pt.type_id = t.id";
		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		return formatPokemonResult(result);
	}

	//TODO
	public Vector<Object[]> getQualifiedPokemonBasedType(int typeID) throws SQLException {
		ResultSet result;

		//NOTE: For this query we only return the given type since that seems
		String query = 
				"SELECT pt.pokemon_id, p.identifier, t.identifier "
						+ "FROM pokemon_types AS pt, types AS t, pokemon_species AS p "
						+ "WHERE t.id = " + typeID + " AND t.id = pt.type_id AND pt.pokemon_id = p.id "
						+ "ORDER BY pt.pokemon_id ASC";

		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		return formatPokemonResult(result);
	}

	public Object[] getGivenPokemonAncestor(int Pid) throws SQLException {
		ResultSet result;
		String query = 
				"SELECT ps.evolves_from_species_id, p.identifier, t.identifier "
						+ "FROM (SELECT evolves_from_species_id "
						+ "FROM pokemon_species " 
						+ "WHERE id = " + Pid + ") AS ps,"
						+ "pokemon_species AS p,"
						+ "pokemon_types AS pt,"
						+ "types AS t "
						+ "WHERE ps.evolves_from_species_id = p.id AND p.id = pt.pokemon_id AND pt.type_id = t.id";

		PreparedStatement ps = db.prepareStatement(query);
		result =  ps.executeQuery();

		Vector<Object[]> pokemon= formatPokemonResult(result);
		//System.out.println(pokemon.size());
		if (pokemon.size() == 0){
			return null;
		}
		return pokemon.get(0);
	}

	// helper func
	public static  Vector<Object[]> formatPokemonResult(ResultSet res) throws SQLException{
		int id = -1, lastid = -1;
		Vector<Object[]> rows = new Vector<>();
		Object rowData[] = new Object[4];

		while (res.next()){
			id = res.getInt(1);
			// Means same pokemon with different type
			if (id != lastid){
				rowData = new Object[4];
				rowData[0] = id;
				rowData[1] = new ImageIcon(PokemonIconDir+id+".png");
				rowData[2] = CommonUtils.capitalize(res.getString(2));
				rowData[3] = CommonUtils.capitalize(res.getString(3));
				rows.add(rowData);
				lastid = id;
			}
			else {
				String types = (rows.lastElement()[3]) + ", " + CommonUtils.capitalize(res.getString(3));
				rowData[3] = types;
				rows.set(rows.size()-1, rowData);
			}
		}
		return (rows);
	}
}
