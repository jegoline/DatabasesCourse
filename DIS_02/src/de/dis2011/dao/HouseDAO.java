package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.House;

public class HouseDAO  implements EntryDAO<House>{

	@Override
	public boolean insert(House entry) {
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
				
				insertSQL = "INSERT INTO house(id, floors, price, garden) VALUES (?, ?, ?, ?)";
			    pstmt = con.prepareStatement(insertSQL, Statement.NO_GENERATED_KEYS);
			    
			    pstmt.setInt(1, entry.getId());
				pstmt.setInt(2, entry.getFloors());
				pstmt.setDouble(3, entry.getPrice());
				pstmt.setBoolean(4, entry.isGarden());
				
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
	public boolean update(House entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(House entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public House load(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
