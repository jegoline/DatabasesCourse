package de.dis2013.editor;

import java.util.Set;

import de.dis2013.core.ImmoService;
import de.dis2013.data.Apartment;
import de.dis2013.data.EstateAgent;
import de.dis2013.data.House;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.util.FormUtil;

/**
 * Klasse für die Menüs zur Verwaltung von Immobilien
 */
public class EstateEditor {
	/// Immobilienservice, der genutzt werden soll
	private ImmoService service;

	/// Wird als Verwalter für die Immobilien eingetragen
	private EstateAgent agent;

	public EstateEditor(ImmoService service, EstateAgent verwalter) {
		this.service = service;
		this.agent = verwalter;
	}

	/**
	 * Zeigt das Immobilien-Hauptmenü
	 */
	public void showImmoMenu() {
		// Menüoptionen
		final int NEW_HOUSE = 0;
		final int EDIT_HOUSE = 1;
		final int DELETE_HOUSE = 2;
		final int NEW_APPARTMENT = 3;
		final int EDIT_APPARTMENT = 4;
		final int DELETE_APPARTMENT = 5;
		final int BACK = 6;

		// Immobilienverwaltungsmenü
		Menu maklerMenu = new Menu("Estate management");
		maklerMenu.addEntry("New house", NEW_HOUSE);
		maklerMenu.addEntry("Update house", EDIT_HOUSE);
		maklerMenu.addEntry("Delete house", DELETE_HOUSE);

		maklerMenu.addEntry("New apartment", NEW_APPARTMENT);
		maklerMenu.addEntry("Update apartment", EDIT_APPARTMENT);
		maklerMenu.addEntry("Delete apartment", DELETE_APPARTMENT);

		maklerMenu.addEntry("Back to main menu", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
			case NEW_HOUSE:
				newHouse();
				break;
			case EDIT_HOUSE:
				editHouse();
				break;
			case DELETE_HOUSE:
				deleteHouse();
				break;
			case NEW_APPARTMENT:
				newAppartment();
				break;
			case EDIT_APPARTMENT:
				editAppartment();
				break;
			case DELETE_APPARTMENT:
				deleteAppartment();
				break;
			case BACK:
				return;
			}
		}
	}

	/**
	 * Abfrage der Daten für ein neues Haus
	 */
	public void newHouse() {
		House h = new House();

		h.setCity(FormUtil.readString("City"));
		h.setPostalCode(FormUtil.readInt("Postal Code"));
		h.setStreet(FormUtil.readString("Street"));
		h.setStreetNumber(FormUtil.readString("Street Number"));
		h.setSquareArea(FormUtil.readInt("Square Area"));
		h.setFloors(FormUtil.readInt("Floors"));
		h.setPrice(FormUtil.readInt("Price"));
		h.setGarden(FormUtil.readBoolean("Garden"));
		h.setAgent(this.agent);

		service.add(h);
	}

	/**
	 * Lässt den Benutzer ein Haus zum bearbeiten auswählen und fragt
	 * anschließend die neuen Daten ab.
	 */
	public void editHouse() {
		// Alle Häuser suchen, die vom Makler verwaltet werden
		Set<House> haeuser = service.getAllEstatesForMakler(House.class, agent);

		// Auswahlmenü für das zu bearbeitende Haus
		HouseSelectionMenu hsm = new HouseSelectionMenu("List of managed houses", haeuser);
		int id = hsm.show();

		// Falls nicht der Eintrag "zurück" gewählt wurde, Haus bearbeiten
		if (id != HouseSelectionMenu.BACK) {
			// Gewähltes Haus laden
			House h = service.getById(House.class, id);

			System.out.println("Haus " + h.getStreet() + " " + h.getStreetNumber() + ", " + h.getPostalCode() + " "
					+ h.getCity() + " wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");

			// Neue Daten abfragen
			String newCity = FormUtil.readString("City (" + h.getCity() + ")");
			int newPostalCode = FormUtil.readInt("Postal code (" + h.getPostalCode() + ")");
			String newStreet = FormUtil.readString("Street (" + h.getStreet() + ")");
			String newSteetNumber = FormUtil.readString("Street number (" + h.getStreetNumber() + ")");
			int newSquareArea = FormUtil.readInt("Square area (" + h.getSquareArea() + ")");
			int newFloors = FormUtil.readInt("Floors (" + h.getFloors() + ")");
			int newPrice = FormUtil.readInt("Price (" + h.getPrice() + ")");
			boolean newGarden = FormUtil.readBoolean("Garden (" + (h.isGarden() ? "j" : "n") + ")");

			// Neue Daten setzen
			if (!newCity.equals(""))
				h.setCity(newCity);

			if (!newStreet.equals(""))
				h.setStreet(newStreet);

			if (!newSteetNumber.equals(""))
				h.setStreetNumber(newSteetNumber);

			if (newPostalCode != 0)
				h.setPostalCode(newPostalCode);

			if (newSquareArea != 0)
				h.setSquareArea(newSquareArea);

			if (newFloors != 0)
				h.setFloors(newFloors);

			if (newPrice != 0)
				h.setPrice(newPrice);

			h.setGarden(newGarden);
			service.update(h);
		}
	}

	/**
	 * Zeigt die Liste von verwalteten Häusern und löscht das entsprechende Haus
	 * nach Auswahl
	 */
	public void deleteHouse() {
		Set<House> haeuser = service.getAllEstatesForMakler(House.class, agent);

		HouseSelectionMenu hsm = new HouseSelectionMenu("List of managed houses", haeuser);
		int id = hsm.show();

		if (id != HouseSelectionMenu.BACK) {
			House h = service.getById(House.class, id);
			service.delete(h);
		}
	}

	/**
	 * Abfrage der Daten für eine neue Wohnung
	 */
	public void newAppartment() {
		Apartment w = new Apartment();

		w.setCity(FormUtil.readString("City"));
		w.setPostalCode(FormUtil.readInt("Postal Code"));
		w.setStreet(FormUtil.readString("Street"));
		w.setStreetNumber(FormUtil.readString("Street number"));
		w.setSquareArea(FormUtil.readInt("Square area"));
		w.setFloor(FormUtil.readInt("Floor"));
		w.setRent(FormUtil.readInt("Rent"));
		w.setBuiltinKitchen(FormUtil.readBoolean("BuildIn Kitchen"));
		w.setBalcony(FormUtil.readBoolean("Balcony"));
		w.setAgent(this.agent);

		service.add(w);
	}
	

	/**
	 * Lässt den Benutzer eine Wohnung zum bearbeiten auswählen und fragt
	 * anschließend die neuen Daten ab.
	 */
	public void editAppartment() {
		// Alle Wohnungen suchen, die vom Makler verwaltet werden
		Set<Apartment> apartments = service.getAllEstatesForMakler(Apartment.class, agent);

		// Auswahlmenü für die zu bearbeitende Wohnung
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("List of managed apartments", apartments);
		int id = asm.show();

		// Falls nicht der Eintrag "zurück" gewählt wurde, Wohnung bearbeiten
		if (id != AppartmentSelectionMenu.BACK) {
			// Wohnung laden
			Apartment w = service.getById(Apartment.class, id);

			System.out.println("Haus " + w.getStreet() + " " + w.getStreetNumber() + ", " + w.getPostalCode() + " "
					+ w.getCity() + " wird bearbeitet. Leere Felder bzw. Eingabe von 0 lässt Feld unverändert.");

			// Neue Daten abfragen
			String newCity = FormUtil.readString("City (" + w.getCity() + ")");
			int newPostalCode = FormUtil.readInt("Postal code (" + w.getPostalCode() + ")");
			String newStreet = FormUtil.readString("Street (" + w.getStreet() + ")");
			String newStreetNumber = FormUtil.readString("Street number (" + w.getStreetNumber() + ")");
			int newSquareArea = FormUtil.readInt("Square area (" + w.getSquareArea() + ")");
			int newFloor = FormUtil.readInt("Floor (" + w.getFloor() + ")");
			int newRent = FormUtil.readInt("Rent (" + w.getRent() + ")");
			boolean newBuiltInKitchen = FormUtil
					.readBoolean("BuiltIn kitchen (" + (w.isBuiltinKitchen() ? "j" : "n") + ")");
			boolean newBalcony = FormUtil.readBoolean("Balcony (" + (w.isBalcony() ? "j" : "n") + ")");

			// Neue Daten setzen
			if (!newCity.equals(""))
				w.setCity(newCity);

			if (!newStreet.equals(""))
				w.setStreet(newStreet);

			if (!newStreetNumber.equals(""))
				w.setStreetNumber(newStreetNumber);

			if (newPostalCode != 0)
				w.setPostalCode(newPostalCode);

			if (newSquareArea != 0)
				w.setSquareArea(newSquareArea);

			if (newFloor != 0)
				w.setFloor(newFloor);

			if (newRent != 0)
				w.setRent(newRent);

			w.setBuiltinKitchen(newBuiltInKitchen);
			w.setBalcony(newBalcony);
			
			service.update(w);
		}
	}

	/**
	 * Zeigt die Liste von verwalteten Wohnungen und löscht die entsprechende
	 * Wohnung nach Auswahl
	 */
	public void deleteAppartment() {
		Set<Apartment> wohnungen = service.getAllEstatesForMakler(Apartment.class, agent);
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("List of managed apartments", wohnungen);
		int id = asm.show();

		if (id != AppartmentSelectionMenu.BACK) {
			Apartment w = service.getById(Apartment.class, id);
			service.delete(w);
		}
	}
}
