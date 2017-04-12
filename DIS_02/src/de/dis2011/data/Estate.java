package de.dis2011.data;

public abstract class Estate {
	private int id = -1;
	private String city;
	private String postalCode;
	private String street;
	private String streetNum;
	private double sqArea;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNum() {
		return streetNum;
	}

	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	public double getSqArea() {
		return sqArea;
	}

	public void setSqArea(double sqArea) {
		this.sqArea = sqArea;
	}
}
