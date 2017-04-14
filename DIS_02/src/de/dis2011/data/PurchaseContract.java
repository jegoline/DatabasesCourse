package de.dis2011.data;

public class PurchaseContract extends Contract {
	private int noOfInstallments;
	private double interestRate;
	
	public int getNoOfInstallments() {
		return noOfInstallments;
	}
	
	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

}
