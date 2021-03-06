package de.dis2013.authentication;

import de.dis2013.core.ImmoService;
import de.dis2013.data.EstateAgent;
import de.dis2013.util.FormUtil;

/**
 * Authentifiziert einen Makler
 */
public class MaklerAuthenticator implements Authenticator {
	private ImmoService service;
	private EstateAgent lastAuthenticatedMakler;
	
	/**
	 * Konstruktor
	 * @param service Immobilien-Service zum Auffinden des entsprechenden Maklers
	 */
	public MaklerAuthenticator(ImmoService service) {
		this.service = service;
	}
	
	/**
	 * Gibt das Makler-Objekt zum letzten erfolgreich authentisierten Makler zurück
	 */
	public EstateAgent getLastAuthenticatedMakler() {
		return this.lastAuthenticatedMakler;
	}
	
	/**
	 * Fragt nach Makler-Login und -Passwort und überprüft die Eingabe
	 */
	public boolean authenticate() {
		boolean ret;
		
		String login = FormUtil.readString("Makler-Login");
		String password = FormUtil.readPassword("Passwort");
		
		EstateAgent m = service.getMaklerByLogin(login);
		
		if(m == null)
			ret = false;
		else
			ret = password.equals(m.getPassword());
		
		lastAuthenticatedMakler = m;
		if(!ret)
			FormUtil.showMessage("Benutzername oder Passwort falsch!");
		
		return ret;
	}
}
