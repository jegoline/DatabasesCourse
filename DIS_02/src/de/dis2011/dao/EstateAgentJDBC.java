package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.EstateAgent;

public class EstateAgentJDBC implements EstateAgentDAO {

	@Override
	public EstateAgent load(int id) {
		try {
			// Hole Verbindung
			Connection con = DB2ConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				EstateAgent ts = new EstateAgent();
				ts.setId(id);
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean insert(EstateAgent entry) {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (entry.getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO estate_agent(name, address, login, password) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setString(1, entry.getName());
				pstmt.setString(2, entry.getAddress());
				pstmt.setString(3, entry.getLogin());
				pstmt.setString(4, entry.getPassword());
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					entry.setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(EstateAgent entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			// Setze Anfrage Parameter
			pstmt.setString(1, entry.getName());
			pstmt.setString(2, entry.getAddress());
			pstmt.setString(3, entry.getLogin());
			pstmt.setString(4, entry.getPassword());
			pstmt.setInt(5, entry.getId());
			pstmt.executeUpdate();

			pstmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(EstateAgent entry) {
		return false;
	}

	@Override
	public boolean loadByLogin(EstateAgent entity) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		try {
			String selectSQL = "select id, name, address, login, password from estate_agent where login=?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, entity.getLogin());
			
			ResultSet result = pstmt.executeQuery();
			
			while (result.next()) {
				assert entity.getLogin().equals(result.getString(4));
				
	            entity.setId(result.getInt(1));
	            entity.setName(result.getString(2));
	            entity.setAddress(result.getString(3));
	            entity.setLogin(result.getString(4));
	            entity.setPassword(result.getString(5));
	        }
			
			pstmt.close();
			return true;
			
		}catch (SQLException e){
			e.printStackTrace();
		}
		return false;
	}
}
