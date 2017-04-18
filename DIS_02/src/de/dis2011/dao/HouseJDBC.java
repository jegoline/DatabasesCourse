package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.House;

public class HouseJDBC implements EstateDAO<House> {

	@Override
	public boolean insert(House entry) {
		assert entry.getId() == -1;
		
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String inserEstateSQL = "INSERT INTO estate(city, postal_code, street, street_number, square_area, estate_agent_id) VALUES (?, ?, ?, ?, ?, ?)";
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
				insertEstate.setInt(6, entry.getManagedByAgent());

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
		String updateEstateSQL = "UPDATE estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area=?, estate_agent_id=? WHERE id = ?";
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
				updateEstate.setInt(6, entry.getManagedByAgent());
				updateEstate.setInt(7, entry.getId());

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
		String deleteEstateSql = "DELETE FROM estate WHERE id = ?";
		
		PreparedStatement deleteEstate = null;
		
		try {
			try {
				deleteEstate = con.prepareStatement(deleteEstateSql);
				deleteEstate.setInt(1, entry.getId());
				int deleted = deleteEstate.executeUpdate();				
				return deleted != 0;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (deleteEstate != null) {
					deleteEstate.close();
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

	@Override
	public List<House> loadAll() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from house left join estate on estate.id = house.id";
				pstmt = con.prepareStatement(selectSQL);

				List<House> result = new ArrayList<House>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					House h = new House();
					h.setId(rs.getInt("id"));
					
					h.setCity(rs.getString("city"));
					h.setStreet(rs.getString("street"));
					h.setStreetNum(rs.getString("street_number"));
					h.setPostalCode(rs.getString("postal_code"));
					h.setSqArea(rs.getDouble("square_area"));
					h.setManagedByAgent(rs.getInt("estate_agent_id"));
					
					h.setFloors(rs.getInt("floors"));
					h.setGarden(rs.getBoolean("garden"));
					h.setPrice(rs.getDouble("price"));
					result.add(h);
				}
				rs.close();
				return result;
				
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
		return Collections.emptyList();
	}

}
