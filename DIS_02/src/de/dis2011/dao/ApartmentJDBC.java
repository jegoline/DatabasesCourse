package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.Apartment;
import de.dis2011.data.DB2ConnectionManager;

public class ApartmentJDBC implements EntryDAO<Apartment>{
	
	@Override
	public Apartment load (int id) {
		return null;
	}
	
	@Override
	public boolean insert(Apartment entry) {
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			if (entry.getId() == -1) {
				String insertSQL = "INSERT INTO estate(city, postal_code, street, street_number, square_area) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
				  
				pstmt.setString(1, entry.getCity());
				pstmt.setString(2, entry.getPostalCode());
				pstmt.setString(3, entry.getStreet());
				pstmt.setString(4, entry.getStreetNum());
				pstmt.setDouble(5, entry.getSqArea());
				
				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					entry.setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
				
				insertSQL = "INSERT INTO app(id, floor, rent, rooms, balcony, builtin_kitchen) VALUES (?, ?, ?, ?, ?, ?)";
			    pstmt = con.prepareStatement(insertSQL, Statement.NO_GENERATED_KEYS);
			    
			    pstmt.setInt(1, entry.getId());
				pstmt.setInt(2, entry.getFloor());
				pstmt.setDouble(3, entry.getRent());
				pstmt.setInt(4, entry.getRooms());
				pstmt.setBoolean(5, entry.isBalcony());
				pstmt.setBoolean(6, entry.isBuiltinKitchen());
				
				pstmt.executeUpdate();
				
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
	public boolean update(Apartment entry) {
		return false;
	}

	@Override
	public boolean delete(Apartment entry) {
		return false;
	}
}
