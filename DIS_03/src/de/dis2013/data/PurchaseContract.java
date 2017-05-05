package de.dis2013.data;

import de.dis2013.util.Helper;

/**
 * PurchaseContract-Bean
 */
public class PurchaseContract extends Contract {
	private int noOfInstallments;
	private int interestRate;
	private House house;
	
	public PurchaseContract() {
		super();
	}
	
	public int getNoOfInstallments() {
		return noOfInstallments;
	}
	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}
	public int getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(int interestRate) {
		this.interestRate = interestRate;
	}
	
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getNoOfInstallments();
		result = prime * result + getInterestRate();
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof PurchaseContract))
			return false;
	
		PurchaseContract other = (PurchaseContract)obj;
	
		if(other.getContractNumber() != getContractNumber() ||
				!Helper.compareObjects(this.getDate(), other.getDate()) ||
				!Helper.compareObjects(this.getPlace(), other.getPlace()) ||
				other.getNoOfInstallments() != getNoOfInstallments() ||
				other.getInterestRate() != getInterestRate())
		{
			return false;
		}
		
		return true;
	}
}
