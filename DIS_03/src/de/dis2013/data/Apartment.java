package de.dis2013.data;

import de.dis2013.util.Helper;


public class Apartment extends Estate {
	private int floor;
	private int rent;
	private int rooms;
	private boolean balcony;
	private boolean builtinKitchen;
	
	public Apartment() {
		super();
	}
	
	public int getFloor() {
		return floor;
	}
	public void setFloor(int stockwerk) {
		this.floor = stockwerk;
	}
	public int getRent() {
		return rent;
	}
	public void setRent(int mietpreis) {
		this.rent = mietpreis;
	}
	public int getRooms() {
		return rooms;
	}
	public void setRooms(int zimmer) {
		this.rooms = zimmer;
	}
	public boolean isBalcony() {
		return balcony;
	}
	public void setBalcony(boolean balkon) {
		this.balcony = balkon;
	}
	public boolean isBuiltinKitchen() {
		return builtinKitchen;
	}
	public void setBuiltinKitchen(boolean ebk) {
		this.builtinKitchen = ebk;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getFloor();
		result = prime * result + getRent();
		result = prime * result + getRooms();
		result = prime * result + ((isBalcony()) ? 1 : 0);
		result = prime * result + ((isBuiltinKitchen()) ? 1 : 0);
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Apartment))
			return false;
	
		Apartment other = (Apartment)obj;
	
		if(other.getId() != getId() ||
				other.getPostalCode() != getPostalCode() ||
				other.getSquareArea() != getSquareArea() ||
				!Helper.compareObjects(this.getCity(), other.getCity()) ||
				!Helper.compareObjects(this.getStreet(), other.getStreet()) ||
				!Helper.compareObjects(this.getStreetNumber(), other.getStreetNumber()) ||
				getFloor() != other.getFloor() ||
				getRent() != other.getRent() ||
				getRooms() != other.getRooms() ||
				isBalcony() != other.isBalcony() ||
				isBuiltinKitchen() != other.isBuiltinKitchen())
		{
			return false;
		}
		
		return true;
	}
}
