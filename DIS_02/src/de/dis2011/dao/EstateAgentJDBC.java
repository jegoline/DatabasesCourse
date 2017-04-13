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
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		try {
			try {
				String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					EstateAgent ts = new EstateAgent();
					ts.setId(id);
					ts.setName(rs.getString("name"));
					ts.setAddress(rs.getString("address"));
					ts.setLogin(rs.getString("login"));
					ts.setPassword(rs.getString("password"));
					rs.close();
					return ts;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean insert(EstateAgent entry) {
		assert entry.getId() == -1;
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		try {
			try {
				String insertSQL = "INSERT INTO estate_agent(name, address, login, password) VALUES (?, ?, ?, ?)";
				pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, entry.getName());
				pstmt.setString(2, entry.getAddress());
				pstmt.setString(3, entry.getLogin());
				pstmt.setString(4, entry.getPassword());

				pstmt.executeUpdate();
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					entry.setId(rs.getInt(1));
				}
				rs.close();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(EstateAgent entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		try {
			try {
				String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE id = ?";
				pstmt = con.prepareStatement(updateSQL);

				pstmt.setString(1, entry.getName());
				pstmt.setString(2, entry.getAddress());
				pstmt.setString(3, entry.getLogin());
				pstmt.setString(4, entry.getPassword());
				pstmt.setInt(5, entry.getId());
				pstmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(EstateAgent entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "delete from estate_agent where id = ?";
				pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, entry.getId());
				pstmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loadByLogin(EstateAgent entity) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		try {
			try {
				String selectSQL = "select id, name, address, login, password from estate_agent where login=?";
				pstmt = con.prepareStatement(selectSQL);
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

				result.close();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
