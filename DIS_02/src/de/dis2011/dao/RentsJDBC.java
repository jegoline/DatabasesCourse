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
import de.dis2011.data.Rents;
import de.dis2011.data.TenancyContract;
import de.dis2011.data.Apartment;
import de.dis2011.data.Person;

public class RentsJDBC implements TransactionDAO<Rents> {

	@Override
	public boolean sign(Rents transaction) {
		//set up objects
		Apartment apartment = transaction.getApartment();
		TenancyContract tenancyContract = transaction.getTenancyContract();
		Person person = transaction.getPerson();
		//
		
		assert transaction.getId() == -1;
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String insertRentsSQL = "INSERT INTO rents(fk_apartment_id, fk_person_id) VALUES (?, ?)";
		String insertContractSQL = "INSERT INTO tenancy_contract(fk_id, contract_no, date, place, start_date, duration, additional_costs) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement insertContract = null;
		PreparedStatement insertRent = null;
		try {
			try {
				con.setAutoCommit(false);
				insertRent = con.prepareStatement(insertRentsSQL, Statement.RETURN_GENERATED_KEYS);
				insertContract = con.prepareStatement(insertContractSQL, Statement.NO_GENERATED_KEYS);
				

				insertRent.setInt(1, apartment.getId());
				insertRent.setInt(2, person.getId());
				
				insertRent.executeUpdate();
				ResultSet rs = insertRent.getGeneratedKeys();
				if (rs.next()) {
					transaction.setId(rs.getInt(1));
				}
				
				insertContract.setInt(1, transaction.getId());
				insertContract.setInt(2, tenancyContract.getContractNo());
				insertContract.setDate(3, new java.sql.Date(tenancyContract.getDate().getTime()));
				insertContract.setString(4, tenancyContract.getPlace());
				insertContract.setDate(5, new java.sql.Date(tenancyContract.getStartDate().getTime()));
				insertContract.setInt(6, tenancyContract.getDuration());
				insertContract.setDouble(7, tenancyContract.getAdditionalCosts());
				insertContract.executeUpdate();
				

				con.commit();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.print("Transaction is being rolled back");
				con.rollback();
			} finally {
				con.setAutoCommit(true);

				if (insertContract != null) {
					insertContract.close();
				}
				if (insertRent != null) {
					insertRent.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}


	@Override
	public List<Rents> loadAll(){
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from rents left join tenancy_contract on rents.id = tenancy_contract.fk_id";
				pstmt = con.prepareStatement(selectSQL);

				List<Rents> result = new ArrayList<Rents>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Rents transaction = new Rents();
					//set up objects
					Apartment apartment = new Apartment();
					TenancyContract tenancyContract = new TenancyContract();
					Person person = new Person();
					//
					
					transaction.setId(rs.getInt("id"));
				
					person.setId(rs.getInt("fk_person_id"));
					
					apartment.setId(rs.getInt("fk_apartment_id"));
					
					tenancyContract.setContractNo(rs.getInt("contract_no"));
					tenancyContract.setAdditionalCosts(rs.getInt("additional_costs"));
					tenancyContract.setDate(rs.getDate("date"));
					tenancyContract.setDuration(rs.getInt("duration"));
					tenancyContract.setPlace(rs.getString("place"));
					tenancyContract.setStartDate(rs.getDate("start_date"));
					
					transaction.setApartment(apartment);
					transaction.setTenancyContract(tenancyContract);
					transaction.setPerson(person);
					
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

