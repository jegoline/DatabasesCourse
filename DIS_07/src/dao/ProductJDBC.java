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
import data.Product;

public class ProductJDBC implements TransactionDAO<Product> {
	
	public List<Product> loadAll(){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from Product_dim";
				pstmt = con.prepareStatement(selectSQL);

				List<Product> result = new ArrayList<Product>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Product transaction = new Product();
					
					transaction.setId(rs.getInt("id"));
					transaction.setGroupName(rs.getString("group_name"));
					transaction.setFamilyName(rs.getString("family_name"));
					transaction.setCategoryName(rs.getString("category_name"));
					transaction.setArticleName(rs.getString("article_name"));
					
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

