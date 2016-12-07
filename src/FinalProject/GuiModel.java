package FinalProject;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class GuiModel extends DefaultTableModel {

	Connection db;
	Vector<Integer> nationalPokeID;   // will hold album ID values corresponding to search results
	int selectedRow;            // current selected row

	public GuiModel() {
		Object[] columns = {"Pokemon", "Type", "Type II"};
		setColumnIdentifiers(columns);
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

	public String getPokemon() {
		if (selectedRow != -1) {
			return (String) getValueAt(selectedRow, 0);
		}
		else {
			return "";
		}
	}
	
	public String getAlbumTitle() {
		if (selectedRow != -1) {
			return (String) getValueAt(selectedRow, 1);
		}
		else {
			return "";
		}
	}

	public String getAlbumYear() {
		if (selectedRow != -1) {
			return (String) getValueAt(selectedRow, 2);
		}
		else {
			return "";
		}
	}

	public void login(String username, String password) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String connectString = "jdbc:postgresql://flowers.mines.edu/csci403";

		db = DriverManager.getConnection(connectString, username, password);
	}

	public void search(String by, String val) throws SQLException {
		ResultSet result;
		if (by.equals("Artist")) {
			result = searchByArtist(val);
		}
		else if (by.equals("Album")) {
			result = searchByAlbum(val);
		}
		else {
			throw new RuntimeException("Invalid search request");
		}
		if (result == null) {
			setNumRows(1);
			setValueAt("Search by " + by + ":", 0, 0);
			setValueAt(val, 0, 1);
			setValueAt("(Not yet implemented)", 0, 2);
		}
		else {
			dataVector = new Vector<Vector<Object>>();
			nationalPokeID = new Vector<>();
			while (result.next()) {
				Vector<Object> row = new Vector<>();
				row.add(result.getString(1));
				row.add(result.getString(2));
				row.add(result.getString(3));
				dataVector.add(row);
				nationalPokeID.add(result.getInt(4));
			}
			this.fireTableDataChanged();
		}
	}

	public ResultSet searchByArtist(String val) throws SQLException {
		// Note ResultSet *must* contain artist name, album title, album year, and album id,
		// in that order for the rest of the code to work properly!
		String query =
				"SELECT ar.name, al.title, al.year, al.id " +
						"FROM artist AS ar, album AS al " +
						"WHERE lower(ar.name) LIKE lower(?) " +
						"AND ar.id = al.artist_id " +
						"ORDER BY ar.name, al.year, al.title";

		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, "%" + val + "%");
		return ps.executeQuery();
	}

	public ResultSet searchByAlbum(String val) throws SQLException {
		String query =
				"SELECT ar.name, al.title, al.year, al.id " +
						"FROM artist AS ar, album AS al " +
						"WHERE lower(al.title) LIKE lower(?) " +
						"AND ar.id = al.artist_id " +
						"ORDER BY ar.name, al.year, al.title";

		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, "%" + val + "%");
		return ps.executeQuery();
	}

	public Vector<String> getArtists() throws SQLException {
		ResultSet result;
		Vector<String> artists = new Vector<>();
		String query =
				"SELECT artist.name " +
						"FROM artist " +
						"ORDER BY artist.name";

		Statement stmt = db.createStatement();
		result = stmt.executeQuery(query);
		while(result.next())
			artists.add(result.getString(1));

		return artists;
	}

	public void insertArtist(String artist) throws SQLException {
		String query =
				"INSERT INTO artist (name) " +
						"VALUES(?)";

		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, artist);
		ps.executeUpdate();
	}

	public void insertAlbum(String artist, String title, String year) throws SQLException {
		String query =
				"INSERT INTO album (artist_id, title, year) " +
						"VALUES(?,?,?)";
		
		PreparedStatement ps = db.prepareStatement(query);
		//ps.setInt(1, getArtistID(artist));
		ps.setString(2, title);
		ps.setInt(3, Integer.parseInt(year));
		ps.executeUpdate();
	}

	public void updateAlbum(int albumID, String title, String year) throws SQLException {
		setValueAt(title, selectedRow, 1);
		setValueAt(year, selectedRow, 2);
		
		String query =
				"UPDATE album " +
				"SET title = ?, " +
					"year = ? " +
				"WHERE id = ?";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, title);
		ps.setInt(2, Integer.parseInt(year));
		ps.setInt(3, albumID);
		ps.executeUpdate();
	}

	public void deleteAlbum(int albumID) throws SQLException {
		String query =
				"DELETE FROM album " +
				"WHERE id = ?";
		PreparedStatement ps = db.prepareStatement(query);
		ps.setInt(1, albumID);
		ps.executeUpdate();
	}
}
