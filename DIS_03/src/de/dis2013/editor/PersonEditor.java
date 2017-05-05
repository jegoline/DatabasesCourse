package de.dis2013.editor;

import de.dis2013.core.ImmoService;
import de.dis2013.data.Person;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;

/**
 * Klasse für die Menüs zur Verwaltung von Personen
 */
public class PersonEditor {
	///Immobilienservice, der genutzt werden soll
	private ImmoService service;
	
	public PersonEditor(ImmoService service) {
		this.service = service;
	}
	
	/**
	 * Zeigt die Personenverwaltung
	 */
	public void showPersonMenu() {
		//Menu options
		final int NEW_PERSON = 0;
		final int EDIT_PERSON = 1;
		final int DELETE_PERSON = 2;
		final int BACK = 3;
		
		//Person administration menu
		Menu maklerMenu = new Menu("Person Administration");
		maklerMenu.addEntry("New person", NEW_PERSON);
		maklerMenu.addEntry("Edit person", EDIT_PERSON);
		maklerMenu.addEntry("Delete person", DELETE_PERSON);
		maklerMenu.addEntry("Back to main menu", BACK);
		
		//Edit input
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_PERSON:
					newPerson();
					break;
				case EDIT_PERSON:
					editPerson();
					break;
				case DELETE_PERSON:
					deletePerson();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Legt eine neue Person an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public void newPerson() {
		Person p = new Person();
		
		p.setFirstName(FormUtil.readString("First name"));
		p.setName(FormUtil.readString("Name"));
		p.setAddress(FormUtil.readString("Address"));
		service.addPerson(p);
		
		System.out.println("Person with ID "+p.getId()+" was added.");
	}
	
	/**
	 * Editiert eine Person, nachdem der Benutzer sie ausgewählt hat
	 */
	public void editPerson() {
		//Person selection menu
		Menu personSelectionMenu = new PersonSelectionMenu("Edit Person", service.getAllPersons());
		int id = personSelectionMenu.show();
		
		//Person edit
		if(id != PersonSelectionMenu.BACK) {
			//Person load
			Person p = service.getPersonById(id);
			System.out.println("Person "+p.getFirstName()+" "+p.getName()+" was edited. Empty fields remain unchanged.");
			
			//New Data acquisition
			String newVorname = FormUtil.readString("First name ("+p.getFirstName()+")");
			String newNachname = FormUtil.readString("Name ("+p.getName()+")");
			String newAddress = FormUtil.readString("Address ("+p.getAddress()+")");
			
			//Change
			if(!newVorname.equals(""))
				p.setFirstName(newVorname);
			if(!newNachname.equals(""))
				p.setName(newNachname);
			if(!newAddress.equals(""))
				p.setAddress(newAddress);
		}
	}
	
	/**
	 * Löscht eine Person, nachdem der Benutzer
	 * die entprechende ID eingegeben hat.
	 */
	public void deletePerson() {
		//Auswahl der Person
		Menu personSelectionMenu = new PersonSelectionMenu("Delete Person", service.getAllPersons());
		int id = personSelectionMenu.show();
		
		//Löschen, falls nicht "zurück" gewählt wurde
		if(id != PersonSelectionMenu.BACK) {
			Person p = service.getPersonById(id);
			service.deletePerson(p);
		}
	}
}
