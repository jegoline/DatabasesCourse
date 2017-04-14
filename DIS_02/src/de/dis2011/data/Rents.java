package de.dis2011.data;

public class Rents {
	private int id = -1;
	private Apartment apartment;
	private Person person;
	private TenancyContract tenancyContract;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Apartment getApartment() {
		return apartment;
	}
	
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public TenancyContract getTenancyContract() {
		return tenancyContract;
	}
	
	public void setTenancyContract(TenancyContract tenancyContract) {
		this.tenancyContract = tenancyContract;
	}
	
}
