package de.dis2013.core;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.dis2013.data.House;
import de.dis2013.data.Estate;
import de.dis2013.data.PurchaseContract;
import de.dis2013.data.EstateAgent;
import de.dis2013.data.TenancyContract;
import de.dis2013.data.Person;
import de.dis2013.data.Apartment;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 * 
 * TODO: Aktuell werden alle Daten im Speicher gehalten. Ziel der Übung
 * ist es, schrittweise die Datenverwaltung in die Datenbank auszulagern.
 * Wenn die Arbeit erledigt ist, werden alle Sets dieser Klasse überflüssig.
 */
public class ImmoService {
	//Datensätze im Speicher
	private Set<EstateAgent> makler = new HashSet<EstateAgent>();
	private Set<Person> persons = new HashSet<Person>();
	private Set<House> haeuser = new HashSet<House>();
	private Set<Apartment> wohnungen = new HashSet<Apartment>();
	private Set<TenancyContract> tenancyContracts = new HashSet<TenancyContract>();
	private Set<PurchaseContract> purchaseContracts = new HashSet<PurchaseContract>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Finde einen Makler mit gegebener Id
	 * @param id Die ID des Maklers
	 * @return Makler mit der ID oder null
	 */
	public EstateAgent getMaklerById(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		EstateAgent agent = (EstateAgent) session.get(EstateAgent.class, id);
		session.getTransaction().commit();
        return agent;
	}
	
	/**
	 * Finde einen Makler mit gegebenem Login
	 * @param login Der Login des Maklers
	 * @return Makler mit der ID oder null
	 */
	public EstateAgent getMaklerByLogin(String login) {
		Iterator<EstateAgent> it = makler.iterator();
		
		while(it.hasNext()) {
			
			EstateAgent m = it.next();
			if(m.getLogin().equals(login))
				return m;
		}
		
		return null;
	}
	
	/**
	 * Gibt alle Makler zurück
	 */
	public Set<EstateAgent> getAllMakler() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<EstateAgent> agents = session.createCriteria(EstateAgent.class).list();
		session.getTransaction().commit();
		
		return new HashSet<EstateAgent>(agents);
	}
	
	/**
	 * Finde eine Person mit gegebener Id
	 * @param id Die ID der Person
	 * @return Person mit der ID oder null
	 */
	public Person getPersonById(int id) {
		Iterator<Person> it = persons.iterator();
		
		while(it.hasNext()) {
			Person p = it.next();
			
			if(p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	/**
	 * Fügt einen Makler hinzu
	 * @param m Der Makler
	 */
	public void addMakler(EstateAgent m) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(m);
		session.getTransaction().commit();
	}
	
	/**
	 * Löscht einen Makler
	 * @param m Der Makler
	 */
	public void deleteMakler(EstateAgent m) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(m);
		session.getTransaction().commit();
	}
	
	/**
	 * Fügt eine Person hinzu
	 * @param p Die Person
	 */
	public void addPerson(Person p) {
		persons.add(p);
	}
	
	/**
	 * Gibt alle Personen zurück
	 */
	public Set<Person> getAllPersons() {
		return persons;
	}
	
	/**
	 * Löscht eine Person
	 * @param p Die Person
	 */
	public void deletePerson(Person p) {
		persons.remove(p);
	}
	
	/**
	 * Fügt ein Haus hinzu
	 * @param h Das Haus
	 */
	public void addHaus(House h) {
		haeuser.add(h);
	}
	
	/**
	 * Gibt alle Häuser eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Häuser, die vom Makler verwaltet werden
	 */
	public Set<House> getAllHaeuserForMakler(EstateAgent m) {
		Set<House> ret = new HashSet<House>();
		Iterator<House> it = haeuser.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getVerwalter().equals(m))
				ret.add(h);
		}
		
		return ret;
	}
	
	/**
	 * Findet ein Haus mit gegebener ID
	 * @param m Der Makler
	 * @return Das Haus oder null, falls nicht gefunden
	 */
	public House getHausById(int id) {
		Iterator<House> it = haeuser.iterator();
		
		while(it.hasNext()) {
			House h = it.next();
			
			if(h.getId() == id)
				return h;
		}
		
		return null;
	}
	
	/**
	 * Löscht ein Haus
	 * @param p Das Haus
	 */
	public void deleteHouse(House h) {
		haeuser.remove(h);
	}
	
	/**
	 * Fügt eine Wohnung hinzu
	 * @param w die Wohnung
	 */
	public void addWohnung(Apartment w) {
		wohnungen.add(w);
	}
	
	/**
	 * Gibt alle Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Wohnungen, die vom Makler verwaltet werden
	 */
	public Set<Apartment> getAllWohnungenForMakler(EstateAgent m) {
		Set<Apartment> ret = new HashSet<Apartment>();
		Iterator<Apartment> it = wohnungen.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getVerwalter().equals(m))
				ret.add(w);
		}
		
		return ret;
	}
	
	/**
	 * Findet eine Wohnung mit gegebener ID
	 * @param id Die ID
	 * @return Die Wohnung oder null, falls nicht gefunden
	 */
	public Apartment getWohnungById(int id) {
		Iterator<Apartment> it = wohnungen.iterator();
		
		while(it.hasNext()) {
			Apartment w = it.next();
			
			if(w.getId() == id)
				return w;
		}
		
		return null;
	}
	
	/**
	 * Löscht eine Wohnung
	 * @param p Die Wohnung
	 */
	public void deleteWohnung(Apartment w) {
		wohnungen.remove(w);
	}
	
	
	/**
	 * Fügt einen Mietvertrag hinzu
	 * @param w Der Mietvertrag
	 */
	public void addTenancyContract(TenancyContract m) {
		tenancyContracts.add(m); 
	}
	
	/**
	 * Fügt einen Kaufvertrag hinzu
	 * @param w Der Kaufvertrag
	 */
	public void addPurchaseContract(PurchaseContract k) {
		purchaseContracts.add(k);
	}
	
	/**
	 * Gibt alle Mietverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Mietverträge, die zu Wohnungen gehören, die vom Makler verwaltet werden
	 */
	public Set<TenancyContract> getAllTenancyContractsForEstateAgent(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract v = it.next();
			
			if(v.getApartment().getVerwalter().equals(m))
				ret.add(v);
		}
		
		return ret;
	}
	
	/**
	 * Gibt alle Kaufverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Kaufverträge, die zu Häusern gehören, die vom Makler verwaltet werden
	 */
	public Set<PurchaseContract> getAllPurchaseContractsForEstateAgent(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getVerwalter().equals(m))
				ret.add(k);
		}
		
		return ret;
	}
	
	/**
	 * Findet einen Mietvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Mietvertrag oder null, falls nicht gefunden
	 */
	public TenancyContract getTenancyContractById(int id) {
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract m = it.next();
			
			if(m.getId() == id)
				return m;
		}
		
		return null;
	}
	
	/**
	 * Findet alle Mietverträge, die Wohnungen eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Mietverträgen
	 */
	public Set<TenancyContract> getTenancyContractByAgent(EstateAgent m) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = tenancyContracts.iterator();
		
		while(it.hasNext()) {
			TenancyContract mv = it.next();
			
			if(mv.getApartment().getVerwalter().getId() == m.getId())
				ret.add(mv);
		}
		
		return ret;
	}
	
	/**
	 * Findet alle Kaufverträge, die Häuser eines gegebenen Verwalters betreffen
	 * @param id Der Verwalter
	 * @return Set aus Kaufverträgen
	 */
	public Set<PurchaseContract> getPurchaseContractByAgent(EstateAgent m) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getVerwalter().getId() == m.getId())
				ret.add(k);
		}
		
		return ret;
	}
	
	/**
	 * Findet einen Kaufvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Kaufvertrag oder null, falls nicht gefunden
	 */
	public PurchaseContract getPurchaseContractById(int id) {
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getId() == id)
				return k;
		}
		
		return null;
	}
	
	/**
	 * Löscht einen Mietvertrag
	 * @param m Der Mietvertrag
	 */
	public void deleteTenancyContract(TenancyContract m) {
		tenancyContracts.remove(m); //was wohnungen.remove(m); This is pretty dumb. Why is it there anyways? 
	}
	
	/**
	 * Fügt einige Testdaten hinzu
	 */
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 9");
		m.setLogin("max");
		m.setPassword("max");
		
		//TODO: Dieser Makler wird im Speicher und der DB gehalten
		this.addMakler(m);
		session.save(m);
		session.getTransaction().commit();

		session.beginTransaction();
		
		Person p1 = new Person();
		p1.setAddress("Informatikum");
		p1.setName("Mustermann");
		p1.setFirstName("Erika");
		
		
		Person p2 = new Person();
		p2.setAddress("Reeperbahn 9");
		p2.setName("Albers");
		p2.setFirstName("Hans");
		
		session.save(p1);
		session.save(p2);
		
		//TODO: Diese Personen werden im Speicher und der DB gehalten
		this.addPerson(p1);
		this.addPerson(p2);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session.beginTransaction();
		House h = new House();
		h.setOrt("Hamburg");
		h.setPlz(22527);
		h.setStrasse("Vogt-Kölln-Straße");
		h.setHausnummer("2a");
		h.setFlaeche(384);
		h.setStockwerke(5);
		h.setKaufpreis(10000000);
		h.setGarten(true);
		h.setVerwalter(m);
		
		session.save(h);
		
		//TODO: Dieses Haus wird im Speicher und der DB gehalten
		this.addHaus(h);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session = sessionFactory.openSession();
		session.beginTransaction();
		EstateAgent m2 = (EstateAgent)session.get(EstateAgent.class, m.getId());
		Set<Estate> immos = m2.getEstates();
		Iterator<Estate> it = immos.iterator();
		
		while(it.hasNext()) {
			Estate i = it.next();
			System.out.println("Immo: "+i.getOrt());
		}
		session.close();
		
		Apartment w = new Apartment();
		w.setOrt("Hamburg");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);
		
		w = new Apartment();
		w.setOrt("Berlin");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);
		
		PurchaseContract kv = new PurchaseContract();
		kv.setHouse(h);
		kv.setContractingPerson(p1);
		kv.setContractNumber(9234);
		kv.setDate(new Date(System.currentTimeMillis()));
		kv.setPlace("Hamburg");
		kv.setNoOfInstallments(5);
		kv.setInterestRate(4);
		this.addPurchaseContract(kv);
		
		TenancyContract mv = new TenancyContract();
		mv.setApartment(w);
		mv.setContractingPerson(p2);
		mv.setContractNumber(23112);
		mv.setDate(new Date(System.currentTimeMillis()-1000000000));
		mv.setPlace("Berlin");
		mv.setStartDate(new Date(System.currentTimeMillis()));
		mv.setAdditionalCosts(65);
		mv.setDuration(36);
		this.addTenancyContract(mv);
	}
}
