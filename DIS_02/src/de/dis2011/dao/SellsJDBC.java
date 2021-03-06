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
import de.dis2011.data.Sells;
import de.dis2011.data.TenancyContract;
import de.dis2011.data.PurchaseContract;
import de.dis2011.data.Rents;
import de.dis2011.data.House;
import de.dis2011.data.Person;

public class SellsJDBC implements TransactionDAO<Sells> {

	@Override
	public boolean sign(Sells transaction) {
		//set up objects
		House house = transaction.getHouse();
		PurchaseContract purchaseContract = transaction.getPurchaseContract();
		Person person = transaction.getPerson();
		//
		
		assert transaction.getId() == -1;
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		
		String insertRentsSQL = "INSERT INTO sells(fk_house_id, fk_person_id) VALUES (?, ?)";
		String insertContractSQL = "INSERT INTO purchase_contract(fk_id, contract_no, date, place, no_of_installments, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";
		
		PreparedStatement insertContract = null;
		PreparedStatement insertRent = null;
		try {
			try {
				con.setAutoCommit(false);
				insertRent = con.prepareStatement(insertRentsSQL, Statement.RETURN_GENERATED_KEYS);
				insertContract = con.prepareStatement(insertContractSQL, Statement.NO_GENERATED_KEYS);
				

				insertRent.setInt(1, house.getId());
				insertRent.setInt(2, person.getId());
				
				insertRent.executeUpdate();
				ResultSet rs = insertRent.getGeneratedKeys();
				if (rs.next()) {
					transaction.setId(rs.getInt(1));
				}
				
				insertContract.setInt(1, transaction.getId());
				insertContract.setInt(2, purchaseContract.getContractNo());
				insertContract.setDate(3, new java.sql.Date(purchaseContract.getDate().getTime()));
				insertContract.setString(4, purchaseContract.getPlace());
				insertContract.setInt(5, purchaseContract.getNoOfInstallments());
				insertContract.setDouble(6, purchaseContract.getInterestRate());
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
	public List<Sells> loadAll() {
		Connection con = DB2ConnectionManager.getInstance().getConnection();
		PreparedStatement pstmt = null;
		
		try {
			try {
				String selectSQL = "select * from sells left join purchase_contract on sells.id = purchase_contract.fk_id";
				pstmt = con.prepareStatement(selectSQL);

				List<Sells> result = new ArrayList<Sells>();
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Sells transaction = new Sells();
					//set up objects
					House house = new House();
					PurchaseContract purchaseContract = new PurchaseContract();
					Person person = new Person();
					//
					
					transaction.setId(rs.getInt("id"));
				
					person.setId(rs.getInt("fk_person_id"));
					
					house.setId(rs.getInt("fk_house_id"));
					
					purchaseContract.setContractNo(rs.getInt("contract_no"));
					purchaseContract.setNoOfInstallments(rs.getInt("no_of_installments"));
					purchaseContract.setDate(rs.getDate("date"));
					purchaseContract.setPlace(rs.getString("place"));
					purchaseContract.setInterestRate(rs.getDouble("interest_rate"));
					
					transaction.setHouse(house);
					transaction.setPurchaseContract(purchaseContract);
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
