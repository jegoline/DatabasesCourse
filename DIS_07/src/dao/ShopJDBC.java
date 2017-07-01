package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import etl.DB2ConnectionManager;
import data.Shop;

public class ShopJDBC implements TransactionDAO<Shop> {
	
	public List<Shop> loadAll(){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from shop_dim";
				pstmt = con.prepareStatement(selectSQL);

				List<Shop> result = new ArrayList<Shop>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Shop transaction = new Shop();
					
					transaction.setId(rs.getInt("id"));
					transaction.setCityName(rs.getString("stadt_name"));
					transaction.setCountryName(rs.getString("land_name"));
					transaction.setRegionName(rs.getString("region_name"));
					transaction.setShopName(rs.getString("shop_name"));
					
					result.add(transaction);
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

