package de.dis2011.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dis2011.data.DB2ConnectionManager;
import de.dis2011.data.Person;

public class PersonJDBC implements PersonDAO<Person> {

	@Override
	public boolean insert(Person person) {
		assert person.getId() == -1;
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		try {
			try {
				String insertPersonSQL = "INSERT INTO person(first_name, name, address) VALUES (?, ?, ?)";
				pstmt = con.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS);

				pstmt.setString(1, person.getName());
				pstmt.setString(2, person.getFirstName());
				pstmt.setString(3, person.getAddress());

				pstmt.executeUpdate();
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					person.setId(rs.getInt(1));
				}
				rs.close();
				return true;

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
		return false;
	}

}
