package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.House;

public class HouseJDBC implements EntryDAO<House> {

	@Override
	public boolean insert(House entry) {
		assert entry.getId() == -1;
		
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String inserEstateSQL = "INSERT INTO estate(city, postal_code, street, street_number, square_area) VALUES (?, ?, ?, ?, ?)";
		String insertHouseSQL = "INSERT INTO house(id, floors, price, garden) VALUES (?, ?, ?, ?)";

		PreparedStatement insertEstate = null;
		PreparedStatement insertHouse = null;
		try {
			try {
				con.setAutoCommit(false);
				insertEstate = con.prepareStatement(inserEstateSQL, Statement.RETURN_GENERATED_KEYS);
				insertHouse = con.prepareStatement(insertHouseSQL, Statement.NO_GENERATED_KEYS);

				insertEstate.setString(1, entry.getCity());
				insertEstate.setString(2, entry.getPostalCode());
				insertEstate.setString(3, entry.getStreet());
				insertEstate.setString(4, entry.getStreetNum());
				insertEstate.setDouble(5, entry.getSqArea());

				insertEstate.executeUpdate();
				ResultSet rs = insertEstate.getGeneratedKeys();
				if (rs.next()) {
					entry.setId(rs.getInt(1));
				}

				insertHouse.setInt(1, entry.getId());
				insertHouse.setInt(2, entry.getFloors());
				insertHouse.setDouble(3, entry.getPrice());
				insertHouse.setBoolean(4, entry.isGarden());
						
				insertHouse.executeUpdate();

				con.commit();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (insertEstate != null) {
					insertEstate.close();
				}
				if (insertHouse != null) {
					insertHouse.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean update(House entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		String updateEstateSQL = "UPDATE estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area=? WHERE id = ?";
		String updateHouseSQL = "UPDATE house SET floors = ?, price = ?, garden = ? WHERE id = ?";

		PreparedStatement updateEstate = null;
		PreparedStatement updateHouse = null;
		try {
			try {
				con.setAutoCommit(false);
				updateEstate = con.prepareStatement(updateEstateSQL);
				updateHouse = con.prepareStatement(updateHouseSQL);

				updateEstate.setString(1, entry.getCity());
				updateEstate.setString(2, entry.getPostalCode());
				updateEstate.setString(3, entry.getStreet());
				updateEstate.setString(4, entry.getStreetNum());
				updateEstate.setDouble(5, entry.getSqArea());
				updateEstate.setInt(6, entry.getId());

				updateHouse.setInt(1, entry.getFloors());
				updateHouse.setDouble(2, entry.getPrice());
				updateHouse.setBoolean(3, entry.isGarden());
				updateHouse.setInt(4, entry.getId());
							
				updateEstate.executeUpdate();
				updateHouse.executeUpdate();
				con.commit();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (updateEstate != null) {
					updateEstate.close();
				}
				if (updateHouse != null) {
					updateHouse.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(House entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String deleteHouseSql = "DELETE FROM house WHERE id = ?";
		String deleteEstateSql = "DELETE FROM estate WHERE id = ?";
		
		PreparedStatement deleteEstate = null;
		PreparedStatement deleteHouse = null;
		
		try {
			try {
				con.setAutoCommit(false);
				deleteEstate = con.prepareStatement(deleteEstateSql);
				deleteHouse = con.prepareStatement(deleteHouseSql);

				deleteEstate.setInt(1, entry.getId());
				deleteHouse.setInt(1, entry.getId());

				deleteHouse.executeUpdate();
				deleteEstate.executeUpdate();
				
				con.commit();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (deleteEstate != null) {
					deleteEstate.close();
				}
				if (deleteHouse != null) {
					deleteHouse.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public House load(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
