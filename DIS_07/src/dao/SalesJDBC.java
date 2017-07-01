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
import data.Sales;
import data.Shop;
import data.Time;

public class SalesJDBC implements TransactionDAO<Sales> {

	public List<Sales> loadAll(){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from sales "
						+ "join shop_dim "
						+ "on sales.fk_shop_id = shop_dim.id " 
						+ "join product_dim "
						+ "on sales.fk_product_id = product_dim.id "  
						+ "join time_dim "
						+ "on sales.fk_time_id = time_dim.id ";
				pstmt = con.prepareStatement(selectSQL);

				List<Sales> result = new ArrayList<Sales>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Sales transaction = new Sales();
					//set up objects
					Shop shop = new Shop();
					Product product = new Product();
					Time time = new Time();
//TODO
					
//					transaction.setId(rs.getInt("id"));
//				
//					person.setId(rs.getInt("fk_person_id"));
//					
//					apartment.setId(rs.getInt("fk_apartment_id"));
//					
//					tenancyContract.setContractNo(rs.getInt("contract_no"));
//					tenancyContract.setAdditionalCosts(rs.getInt("additional_costs"));
//					tenancyContract.setDate(rs.getDate("date"));
//					tenancyContract.setDuration(rs.getInt("duration"));
//					tenancyContract.setPlace(rs.getString("place"));
//					tenancyContract.setStartDate(rs.getDate("start_date"));
//					
					transaction.setTurnover(rs.getDouble("amount"));
					transaction.setItemSold(rs.getInt("items_sold"));
					transaction.setShop(shop);
					transaction.setProduct(product);
					transaction.setTime(time);
					
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

