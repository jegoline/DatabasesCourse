package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dis2011.data.Apartment;
import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.House;

public class ApartmentJDBC implements EstateDAO<Apartment> {

	@Override
	public Apartment load(int id) {
		return null;
	}

	@Override
	public boolean insert(Apartment entry) {
		assert entry.getId() == -1;
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		String inserEstateSQL = "INSERT INTO estate(city, postal_code, street, street_number, square_area, estate_agent_id) VALUES (?, ?, ?, ?, ?, ?)";
		String insertHouseSQL = "INSERT INTO apartment(id, floor, rent, rooms, balcony, builtin_kitchen) VALUES (?, ?, ?, ?, ?, ?)";

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
				insertHouse.setInt(2, entry.getFloor());
				insertHouse.setDouble(3, entry.getRent());
				insertHouse.setInt(4, entry.getRooms());
				insertHouse.setBoolean(5, entry.isBalcony());
				insertHouse.setBoolean(6, entry.isBuiltinKitchen());

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
	public boolean update(Apartment entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		String updateEstateSQL = "UPDATE estate SET city = ?, postal_code = ?, street = ?, street_number = ?, square_area=?, estate_agent_id=? WHERE id = ?";
		String updateHouseSQL = "UPDATE apartment SET floor = ?, rent = ?, rooms = ?, balcony=?, builtin_kitchen=? WHERE id = ?";

		PreparedStatement updateEstate = null;
		PreparedStatement updateApt = null;
		try {
			try {
				con.setAutoCommit(false);
				updateEstate = con.prepareStatement(updateEstateSQL);
				updateApt = con.prepareStatement(updateHouseSQL);

				updateEstate.setString(1, entry.getCity());
				updateEstate.setString(2, entry.getPostalCode());
				updateEstate.setString(3, entry.getStreet());
				updateEstate.setString(4, entry.getStreetNum());
				updateEstate.setDouble(5, entry.getSqArea());
				updateEstate.setInt(6, entry.getManagedByAgent());
				updateEstate.setInt(7, entry.getId());

				updateApt.setInt(1, entry.getFloor());
				updateApt.setDouble(2, entry.getRent());
				updateApt.setInt(3, entry.getRooms());
				updateApt.setBoolean(4, entry.isBalcony());
				updateApt.setBoolean(5, entry.isBuiltinKitchen());
				updateApt.setInt(6, entry.getId());

				int updEst = updateEstate.executeUpdate();
				int updApt = updateApt.executeUpdate();

				con.commit();
				return (updEst != 0 || updApt != 0);
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (updateEstate != null) {
					updateEstate.close();
				}
				if (updateApt != null) {
					updateApt.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Apartment entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		String deleteEstateSql = "DELETE FROM estate WHERE id = ?";

		PreparedStatement deleteEstate = null;
		try {
			try {
				deleteEstate = con.prepareStatement(deleteEstateSql);
				deleteEstate.setInt(1, entry.getId());
				return deleteEstate.executeUpdate() != 0;
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
	public List<Apartment> loadAll() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from apartment left join estate on estate.id = apartment.id";
				pstmt = con.prepareStatement(selectSQL);

				List<Apartment> result = new ArrayList<Apartment>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Apartment h = new Apartment();
					h.setId(rs.getInt("id"));
					
					h.setCity(rs.getString("city"));
					h.setStreet(rs.getString("street"));
					h.setStreetNum(rs.getString("street_number"));
					h.setPostalCode(rs.getString("postal_code"));
					h.setSqArea(rs.getDouble("square_area"));
					h.setManagedByAgent(rs.getInt("estate_agent_id"));
					
					h.setFloor(rs.getInt("floor"));
					h.setBalcony(rs.getBoolean("balcony"));
					h.setRent(rs.getDouble("rent"));
					h.setRooms(rs.getInt("rooms"));
					h.setBuiltinKitchen(rs.getBoolean("builtin_kitchen"));

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
