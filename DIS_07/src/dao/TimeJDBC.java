package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import etl.DB2ConnectionManager;
import data.Time;

public class TimeJDBC implements TransactionDAO<Time> {
	
	
	
	public Time insert(Time entry) {
		assert entry.getId() == -1;
		
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String insertSQL = "INSERT INTO time_dim(year, quarter, month, date) VALUES (?, ?, ?, ?)";

		PreparedStatement insertTime = null;
		try {
			try {
				con.setAutoCommit(false);
				insertTime = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);

				insertTime.setString(1, entry.getYear());
				insertTime.setString(2, entry.getQuarter());
				insertTime.setString(3, entry.getMonth());
				insertTime.setDate(4, new java.sql.Date(entry.getDate().getTime()));
						
				insertTime.executeUpdate();
				
				ResultSet rs = insertTime.getGeneratedKeys();
				if (rs.next()) {
					entry.setId(rs.getInt(1));
				}

				con.commit();
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (insertTime != null) {
					insertTime.close();
				}
				if (insertTime != null) {
					insertTime.close();
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return entry;
	}
	
	public List<Time> loadAll(){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from time_dim";
				pstmt = con.prepareStatement(selectSQL);

				List<Time> result = new ArrayList<Time>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Time transaction = new Time();
					
					transaction.setId(rs.getInt("id"));
					transaction.setDate(convertFromSQLDateToJAVADate(rs.getDate("date")));
					transaction.setMonth(rs.getString("month"));
					transaction.setQuarter(rs.getString("quarter"));
					transaction.setYear(rs.getString("year"));
					
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

	private static java.util.Date convertFromSQLDateToJAVADate(
            java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

}

