package de.dis2013.editor;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.core.ImmoService;
import de.dis2013.data.House;
import de.dis2013.data.PurchaseContract;
import de.dis2013.data.EstateAgent;
import de.dis2013.data.TenancyContract;
import de.dis2013.data.Person;
import de.dis2013.data.Apartment;
import de.dis2013.menu.AppartmentSelectionMenu;
import de.dis2013.menu.HouseSelectionMenu;
import de.dis2013.menu.Menu;
import de.dis2013.menu.PersonSelectionMenu;
import de.dis2013.util.FormUtil;
import de.dis2013.util.Helper;

/**
 * Klasse für die Menüs zur Verwaltung von Verträgen
 */
public class ContractsEditor {
	///Immobilien-Service, der genutzt werden soll
	private ImmoService service;
	
	///Makler, zu dessen Immobilien Verträge geschlossen werden dürfen
	private EstateAgent estateAgent;
	
	public ContractsEditor(ImmoService service, EstateAgent estateAgent) {
		this.service = service;
		this.estateAgent = estateAgent;
	}
	
	/**
	 * Vertragsmenü
	 */
	public void showContractMenu() {
		//Menüoptionen
		final int NEW_LEASING_CONTRACT = 0;
		final int NEW_SALE_CONTRACT = 1;
		final int SHOW_CONTRACTS = 2;
		final int BACK = 3;
		
		//Vertragsverwaltung
		Menu maklerMenu = new Menu("Contract Administration");
		maklerMenu.addEntry("New rental contract", NEW_LEASING_CONTRACT);
		maklerMenu.addEntry("New purchase contract", NEW_SALE_CONTRACT);
		maklerMenu.addEntry("View contracts", SHOW_CONTRACTS);
		
		maklerMenu.addEntry("Back to main menu", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_LEASING_CONTRACT:
					newTenancyContract();
					break;
				case NEW_SALE_CONTRACT:
					newPurchaseContract();
					break;
				case SHOW_CONTRACTS:
					viewContracts();
					break;
				case BACK:
					return;
			}
		}
	}
	
	public void viewContracts() {
		//Mietverträge anzeigen
		System.out.println("Rental Contracts\n-----------------");
		Set<TenancyContract> mvs = service.getAllTenancyContractsForEstateAgent(TenancyContract.class,estateAgent);
		Iterator<TenancyContract> itmv = mvs.iterator();
		while(itmv.hasNext()) {
			TenancyContract mv = itmv.next();
			System.out.println("Tenancy contract "+mv.getContractNumber()+"\n"+
							"\tSigned on\t		"+Helper.dateToString(mv.getDate())+" in "+mv.getPlace()+"\n"+
							"\tTenant:\t\t		"+mv.getContractingPerson().getFirstName()+" "+mv.getContractingPerson().getName()+"\n"+
							"\tApartment:\t\t	"+mv.getApartment().getStreet()+" "+mv.getApartment().getStreetNumber()+", "+mv.getApartment().getPostalCode()+" "+mv.getApartment().getCity()+"\n"+
							"\tStart date:\t	"+Helper.dateToString(mv.getStartDate())+", Duration: "+mv.getDuration()+" Monate\n"+
							"\tRent:\t			"+mv.getApartment().getRent()+" Euro, Addition costs: "+mv.getAdditionalCosts()+" Euro\n"+
							" ");
		}
		
		System.out.println("");
		
		//Kaufverträge anzeigen
		System.out.println("Purchase Contracts\n-----------------");
		Set<PurchaseContract> kvs = service.getAllPurchaseContractsForEstateAgent(PurchaseContract.class, estateAgent);
		Iterator<PurchaseContract> itkv = kvs.iterator();
		while(itkv.hasNext()) {
			PurchaseContract kv = itkv.next();
			System.out.println("Purchase contract "+kv.getContractNumber()+"\n"+
							"\tSigned on\t		"+Helper.dateToString(kv.getDate())+" in "+kv.getPlace()+"\n"+
							"\tOwner:\t\t		"+kv.getContractingPerson().getFirstName()+" "+kv.getContractingPerson().getName()+"\n"+
							"\tHouse:\t\t		"+kv.getHouse().getStreet()+" "+kv.getHouse().getStreetNumber()+", "+kv.getHouse().getPostalCode()+" "+kv.getHouse().getCity()+"\n"+
							"\tPrice:\t			"+kv.getHouse().getPrice()+" Euro\n"+
							"\tNo. of instal.:\t"+kv.getNoOfInstallments()+", Interest rate: "+kv.getInterestRate()+"%\n");
		}
	}
	
	
	/**
	 * Menü zum anlegen eines neuen Mietvertrags
	 */
	public void newTenancyContract() {
		//Alle Wohnungen des Maklers finden
		Set<Apartment> apartments = service.getAllEstatesForMakler(Apartment.class, estateAgent);
		
		//Auswahlmenü für die Wohnungen 
		AppartmentSelectionMenu asm = new AppartmentSelectionMenu("Choose apartment for contract", apartments);
		int wid = asm.show();
		
		//Falls kein Abbruch: Auswahl der Person
		if(wid != AppartmentSelectionMenu.BACK) {
			//Alle Personen laden
			Set<Person> persons = service.getAllPersons();
			
			//Menü zur Auswahl der Person
			PersonSelectionMenu psm = new PersonSelectionMenu("Choose Person for Contract", persons);
			int pid = psm.show();
			
			//Falls kein Abbruch: Vertragsdaten abfragen und Vertrag anlegen
			if(pid != PersonSelectionMenu.BACK) {
				TenancyContract m = new TenancyContract();
		
				m.setApartment(service.getById(Apartment.class, wid));
				m.setContractingPerson(service.getById(Person.class,pid));
				m.setContractNumber(FormUtil.readInt("Contract number"));
				m.setDate(FormUtil.readDate("Date"));
				m.setPlace(FormUtil.readString("Place"));
				m.setStartDate(FormUtil.readDate("Start date"));
				m.setDuration(FormUtil.readInt("Duration in months"));
				m.setAdditionalCosts(FormUtil.readInt("Additional costs"));
				
				service.add(m);
				
				System.out.println("Rental contract with ID "+m.getId()+" was added.");
			}
		}
	}
	
	/**
	 * Menü zum anlegen eines neuen Kaufvertrags
	 */
	public void newPurchaseContract() {
		//Alle Häuser des Maklers finden
		Set<House> houses = service.getAllEstatesForMakler(House.class, estateAgent);
		
		//Auswahlmenü für das Haus
		HouseSelectionMenu asm = new HouseSelectionMenu("Choose House for Contract", houses);
		int hid = asm.show();
		
		//Falls kein Abbruch: Auswahl der Person
		if(hid != HouseSelectionMenu.BACK) {
			//Alle Personen laden
			Set<Person> persons = service.getAllPersons();
			
			//Menü zur Auswahl der Person
			PersonSelectionMenu psm = new PersonSelectionMenu("Choose Person for Contract", persons);
			int pid = psm.show();
			
			//Falls kein Abbruch: Vertragsdaten abfragen und Vertrag anlegen
			if(pid != PersonSelectionMenu.BACK) {
				PurchaseContract k = new PurchaseContract();
		
				k.setHouse(service.getById(House.class, hid));
				k.setContractingPerson(service.getById(Person.class,pid));
				k.setContractNumber(FormUtil.readInt("Contract No."));
				k.setDate(FormUtil.readDate("Date"));
				k.setPlace(FormUtil.readString("Place"));
				k.setNoOfInstallments(FormUtil.readInt("No. of installments"));
				k.setInterestRate(FormUtil.readInt("Interest rate"));
				
				service.add(k);
				
				System.out.println("Purchase contract with ID "+k.getId()+" was added.");
			}
		}
	}
}
