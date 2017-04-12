package de.dis2011;

import de.dis2011.dao.ApartmentDAO;
import de.dis2011.dao.EntryDAO;
import de.dis2011.dao.EstateAgentDAO;
import de.dis2011.dao.HouseDAO;
import de.dis2011.data.Apartment;
import de.dis2011.data.EstateAgent;
import de.dis2011.data.House;

/**
 * Hauptklasse
 */
public class Main {
	private static EntryDAO<EstateAgent> agentsDAO;
	private static EntryDAO<House> housesDAO;
	private static EntryDAO<Apartment> aptDAO;
	

	public static void main(String[] args) {
		agentsDAO = new EstateAgentDAO();
		housesDAO = new HouseDAO();
		aptDAO = new ApartmentDAO();
		
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_AGENT = 0;
		final int MENU_ESTATE = 1;
		final int QUIT = 2;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Main Menu");
		mainMenu.addEntry("Manage Estate Agents", MENU_AGENT);
		mainMenu.addEntry("Manage Estates", MENU_ESTATE);
		mainMenu.addEntry("Quit", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					showAgentMenu();
					break;
				case MENU_ESTATE:
					showEstateMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	

	public static void showEstateMenu() {
		//Menüoptionen
		final int NEW_HOUSE = 0;
		final int DELETE_HOUSE = 1;
		final int UPDATE_HOUSE = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Manage Estates");
		maklerMenu.addEntry("New House", NEW_HOUSE);
		maklerMenu.addEntry("Delete House", DELETE_HOUSE);
		maklerMenu.addEntry("Update House", UPDATE_HOUSE);
		maklerMenu.addEntry("Back to Main Menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_HOUSE:
					newHouse();
					break;
				case DELETE_HOUSE:
					deleteHouse();
					break;
				case UPDATE_HOUSE:
					updateHouse();
					break;
				case BACK:
					return;
			}
		}
	}

	public static void showAgentMenu() {
		//Menüoptionen
		final int NEW_AGENT = 0;
		final int DELETE_AGENT = 0;
		final int CHANGE_AGENT = 0;
		final int BACK = 1;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Manage Estate Agents");
		maklerMenu.addEntry("New Agent", NEW_AGENT);
		maklerMenu.addEntry("Back to Main Menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newMakler();
					break;
				case BACK:
					return;
			}
		}
	}
	
	public static void newMakler() {
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		agentsDAO.insert(m);
		
		System.out.println("Added Estate Agent with ID " + m.getId());
	}
	
	
	public static void newHouse() {
		House m = new House();
		
		m.setCity(FormUtil.readString("City"));
		m.setPostalCode(FormUtil.readString("Postal Code"));
		m.setStreet(FormUtil.readString("Street"));
		m.setStreetNum(FormUtil.readString("StreetNum"));
		m.setSqArea(FormUtil.readDouble("Area"));
		m.setFloors(FormUtil.readInt("Floors"));
		m.setPrice(FormUtil.readDouble("Price"));
		m.setGarden(FormUtil.readBoolean("Garden"));

		housesDAO.insert(m);
		System.out.println("Added House with ID " + m.getId());
	}
	
	private static void updateHouse() {
		// TODO Auto-generated method stub
		
	}

	private static void deleteHouse() {
		// TODO Auto-generated method stub
		
	}
}
