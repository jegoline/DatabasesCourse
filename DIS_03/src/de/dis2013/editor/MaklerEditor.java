package de.dis2013.editor;

import de.dis2013.core.ImmoService;
import de.dis2013.data.EstateAgent;
import de.dis2013.menu.MaklerSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

public class MaklerEditor {
	private ImmoService service;
	
	public MaklerEditor(ImmoService service) {
		this.service = service;
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int EDIT_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("New agent", NEW_MAKLER);
		maklerMenu.addEntry("Update agent", EDIT_MAKLER);
		maklerMenu.addEntry("Delete agent", DELETE_MAKLER);
		maklerMenu.addEntry("Back to main menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case EDIT_MAKLER:
					editMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public void newMakler() {
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Address"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Password"));
		service.addMakler(m);
		
		System.out.println("Agent with ID " + m.getId() + " was added.");
	}
	
	/**
	 * Berarbeitet einen Makler, nachdem der Benutzer ihn ausgewählt hat
	 */
	public void editMakler() {
		//Menü zum selektieren des Maklers
		Menu maklerSelectionMenu = new MaklerSelectionMenu("Update agent", service.getAllMakler());
		int id = maklerSelectionMenu.show();
		
		//Falls nicht "zurück" gewählt, Makler bearbeiten
		if(id != MaklerSelectionMenu.BACK) {
			//Makler laden
			EstateAgent m = service.getMaklerById(id);
			System.out.println("Agent "+ m.getName()+" will be updated");
			
			//Neue Daten abfragen
			String new_name = FormUtil.readString("Name ("+m.getName()+")");
			String new_address = FormUtil.readString("Address ("+m.getAddress()+")");
			String new_login = FormUtil.readString("Login ("+m.getLogin()+")");
			String new_password = FormUtil.readString("Password ("+m.getPassword()+")");
			
			//Neue Daten setzen
			if(!new_name.equals(""))
				m.setName(new_name);
			if(!new_address.equals(""))
				m.setAddress(new_address);
			if(!new_login.equals(""))
				m.setLogin(new_login);
			if(!new_password.equals(""))
				m.setPassword(new_password);
		}
	}
	
	public void deleteMakler() {
		Menu maklerSelectionMenu = new MaklerSelectionMenu("Delete agent", service.getAllMakler());
		int id = maklerSelectionMenu.show();
		
		if(id != MaklerSelectionMenu.BACK) {
			EstateAgent m = service.getMaklerById(id);
			service.deleteMakler(m);
		}
	}
}
