package de.dis2011.data;

public class Sells {
	private int id = -1;
	private House house;
	private Person person;
	private PurchaseContract purchaseContract;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public House getHouse() {
		return house;
	}
	
	public void setHouse(House house) {
		this.house = house;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public PurchaseContract getPurchaseContract() {
		return purchaseContract;
	}
	
	public void setPurchaseContract(PurchaseContract purchaseContract) {
		this.purchaseContract = purchaseContract;
	}
	
}
