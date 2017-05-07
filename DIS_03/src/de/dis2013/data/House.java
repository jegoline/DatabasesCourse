package de.dis2013.data;

import java.util.Set;

import de.dis2013.util.Helper;

/**
 * Haus-Bean
 */
public class House extends Estate {
	private int floors;
	private double price;
	private boolean garden;
	private PurchaseContract purchaseContract;
	
	public House() {
		super();
	}
	
	public int getFloors() {
		return floors;
	}
	public void setFloors(int floors) {
		this.floors = floors;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double kaufpreis) {
		this.price = kaufpreis;
	}
	public boolean isGarden() {
		return garden;
	}
	public void setGarden(boolean garden) {
		this.garden = garden;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getFloors();
		result = prime * result + Double.hashCode(getPrice());
		result = prime * result + ((isGarden()) ? 1 : 0);
		
		return result;
	}
	
	public PurchaseContract getPurchaseContract() {
		return purchaseContract;
	}

	public void setPurchaseContract(PurchaseContract purchaseContract) {
		this.purchaseContract = purchaseContract;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof House))
			return false;
	
		House other = (House)obj;
	
		if(other.getId() != getId() ||
				other.getPostalCode() != getPostalCode() ||
				other.getSquareArea() != getSquareArea() ||
				!Helper.compareObjects(this.getCity(), other.getCity()) ||
				!Helper.compareObjects(this.getStreet(), other.getStreet()) ||
				!Helper.compareObjects(this.getStreetNumber(), other.getStreetNumber()) ||
				getFloors() != other.getFloors() ||
				getPrice() != other.getPrice() ||
				isGarden() != other.isGarden())
		{
			return false;
		}
		
		return true;
	}
}
