package etl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DB2Loader {
	
	public void loadShops() throws SQLException {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		try {
			String selectSQL = "select shop.name as shop_name, stadt.name as stadt_name, region.name as region_name, land.name as land_name "
				+ "from DB2INST1.shopid as shop "
				+ "join DB2INST1.stadtid as stadt "
				+ "on stadt.stadtid = shop.stadtid "
				+ "join DB2INST1.regionid as region "
				+ "on region.regionid = stadt.regionid "
				+ "join DB2INST1.landid as land "
				+ "on land.landid = region.landid";
			selectStmt = con.prepareStatement(selectSQL);
			ResultSet rs = selectStmt.executeQuery();
			   
			con.setAutoCommit(false);
			String insertProductSQL = "INSERT INTO shop_dim(shop_name, stadt_name, region_name, land_name) VALUES (?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertProductSQL);
			
			while (rs.next()) {
				insertStmt.setString(1, rs.getString("shop_name"));
				insertStmt.setString(2, rs.getString("stadt_name"));
				insertStmt.setString(3, rs.getString("region_name"));
				insertStmt.setString(4, rs.getString("land_name"));
				insertStmt.addBatch();
			}
			
			insertStmt.executeBatch();
			con.commit();
		} finally {
			con.setAutoCommit(true);
			if (selectStmt != null) {
				selectStmt.close();
				insertStmt.close();
			}
		}
	}

	public void loadProducts() throws SQLException {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt= null;
		try {
			String selectSQL = "select article.name as article_name, gr.name as group_name, fam.name as family_name, cat.name as category_name " 
					+ "from DB2INST1.articleid as article "
					+ "join DB2INST1.productgroupid as gr "
					+ "on gr.productgroupid = article.productgroupid " 
					+ "join DB2INST1.productfamilyid as fam "
					+ "on gr.productfamilyid= fam.productfamilyid " 
					+ "join DB2INST1.productcategoryid as cat "
					+ "on cat.productcategoryid = fam.productcategoryid";
			selectStmt = con.prepareStatement(selectSQL);
			ResultSet rs = selectStmt.executeQuery();

			con.setAutoCommit(false);
			String insertProductSQL = "INSERT INTO product_dim(article_name, group_name, family_name, category_name) VALUES (?, ?, ?, ?)";
			insertStmt = con.prepareStatement(insertProductSQL);
			while (rs.next()) {
				insertStmt.setString(1, rs.getString("article_name"));
				insertStmt.setString(2, rs.getString("group_name"));
				insertStmt.setString(3, rs.getString("family_name"));
				insertStmt.setString(4, rs.getString("category_name"));

				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
			con.commit();
		} finally {
			con.setAutoCommit(true);
			if (selectStmt != null) {
				selectStmt.close();
				insertStmt.close();
			}
		}
	}
	
	public void loadSales() throws SQLException {
        String csvFile = "csv/sales_transformed.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitDelimeter = ";";
        int initialSkipLines = 1;
        
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement insertStmt= null;
		
		con.setAutoCommit(false);
		String insertSalesSQL = "INSERT INTO sales"
				+ "(fk_time_id, fk_shop_id, fk_article_id, items_sold, amount) VALUES (?, ?, ?, ?, ?)";
		insertStmt = con.prepareStatement(insertSalesSQL);
		int iterationCounter = 0;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            for(int i=0; i < initialSkipLines; i++){
            	br.readLine();
            }
              
            while ((line = br.readLine()) != null) {

                // use semicolon as separator
                String[] column = line.split(cvsSplitDelimeter);
                
                
                // insert items in SQL statement
                iterationCounter++;
                
    			insertStmt.setInt(1, Integer.parseInt(column[0]));
    			insertStmt.setInt(2, Integer.parseInt(column[1]));
    			insertStmt.setInt(3, Integer.parseInt(column[2]));
    			insertStmt.setInt(4, Integer.parseInt(column[3]));
    			insertStmt.setDouble(5, Double.parseDouble(column[4]));
    			
    			insertStmt.addBatch();
            }
            
            insertStmt.executeBatch();
			con.commit();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error on line : " + iterationCounter);
                }
            }
            
            con.setAutoCommit(true);
			if (insertStmt != null) {
				insertStmt.close();
			}
			
        }

    }
}
