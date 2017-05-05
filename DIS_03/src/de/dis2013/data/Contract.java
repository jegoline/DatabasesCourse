package de.dis2013.data;

import java.util.Date;

import de.dis2013.util.Helper;

/**
 * Contracts-Bean
 */
public abstract class Contract {
	private int contractNumber = -1;
	private Date date;
	private String place;
	static int currentId = 0;
	int id;
	private Person contractingPerson;
	
	public Contract() {
		this.id = currentId++;
	}
	
	public int getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date datum) {
		this.date = datum;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String ort) {
		this.place = ort;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getContractingPerson() {
		return contractingPerson;
	}

	public void setContractingPerson(Person contractingPerson) {
		this.contractingPerson = contractingPerson;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
		result = prime * result + ((getPlace() == null) ? 0 : getPlace().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Contract))
			return false;
	
		Contract other = (Contract)obj;
	
		if(other.getContractNumber() != getContractNumber() ||
				!Helper.compareObjects(this.getDate(), other.getDate()) ||
				!Helper.compareObjects(this.getPlace(), other.getPlace()))
		{
			return false;
		}
		
		return true;
	}
}
