package de.dis2013.data;

import java.util.Set;

import de.dis2013.util.Helper;

/**
 * Makler-Bean
 */
public class EstateAgent {
	private int id;
	private String name;
	private String adresse;
	private String login;
	private String passwort;
	static int currentId = 0;
	private Set<Estate> immobilien;
	
	public EstateAgent() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAdresse() {
		return adresse;
	}
	
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPasswort() {
		return passwort;
	}
	
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	
	public Set<Estate> getImmobilien() {
		return immobilien;
	}

	public void setImmobilien(Set<Estate> immobilien) {
		this.immobilien = immobilien;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getAdresse() == null) ? 0 : getAdresse().hashCode());
		result = prime * result + ((getLogin() == null) ? 0 : getLogin().hashCode());
		result = prime * result + ((getPasswort() == null) ? 0 : getPasswort().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof EstateAgent))
			return false;
	
		EstateAgent other = (EstateAgent)obj;
	
		if(other.getId() != getId() ||
				!Helper.compareObjects(getName(), other.getName()) ||
				!Helper.compareObjects(getAdresse(), other.getAdresse()) ||
				!Helper.compareObjects(getLogin(), other.getLogin()) ||
				!Helper.compareObjects(getPasswort(), other.getPasswort()))
		{
			return false;
		}
		
		return true;
	}
}
