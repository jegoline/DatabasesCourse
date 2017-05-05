package de.dis2013.data;

import java.util.Set;

import de.dis2013.util.Helper;

/**
 * Person-Bean
 */
public class Person {
	private int id;
	private String firstName;
	private String name;
	private String address;
	private Set<Contract> contracts;
	
	public Person() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Set<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getFirstName() == null) ? 0 : getFirstName().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Person))
			return false;
	
		Person other = (Person)obj;
	
		if(other.getId() != getId() ||
				!Helper.compareObjects(this.getFirstName(), other.getFirstName()) ||
				!Helper.compareObjects(this.getName(), other.getName()) ||
				!Helper.compareObjects(this.getAddress(), other.getAddress()))
		{
			return false;
		}
		
		return true;
	}
}
