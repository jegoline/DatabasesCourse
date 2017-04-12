package de.dis2011.data;

public class Apartment extends Estate {
	private int floor;
	private double rent;
	private int rooms;
	private boolean balcony;
	private boolean builtinKitchen;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public boolean isBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean isBuiltinKitchen() {
		return builtinKitchen;
	}

	public void setBuiltinKitchen(boolean builtinKitchen) {
		this.builtinKitchen = builtinKitchen;
	}

}
