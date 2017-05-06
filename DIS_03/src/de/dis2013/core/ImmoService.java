package de.dis2013.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import de.dis2013.data.Apartment;
import de.dis2013.data.Estate;
import de.dis2013.data.EstateAgent;
import de.dis2013.data.House;
import de.dis2013.data.Person;
import de.dis2013.data.PurchaseContract;
import de.dis2013.data.TenancyContract;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 * 
 * TODO: Aktuell werden alle Daten im Speicher gehalten. Ziel der Übung
 * ist es, schrittweise die Datenverwaltung in die Datenbank auszulagern.
 * Wenn die Arbeit erledigt ist, werden alle Sets dieser Klasse überflüssig.
 */
public class ImmoService {
	private Set<Person> persons = new HashSet<Person>();
	private Set<TenancyContract> tenancyContracts = new HashSet<TenancyContract>();
	private Set<PurchaseContract> purchaseContracts = new HashSet<PurchaseContract>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	
	/**
	 * Finde einen Makler mit gegebenem Login
	 * 
	 * @param login
	 *            Der Login des Maklers
	 * @return Makler mit der ID oder null
	 */
	public EstateAgent getMaklerByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(EstateAgent.class).add(Restrictions.eq("login", login));
		List<EstateAgent> results = criteria.list();
		assert results.size() <= 1;
		session.getTransaction().commit();
		if (results.size() != 0)
			return results.get(0);
		
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
	
	public <T> void add(T element) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save(element);
		session.getTransaction().commit();
	}

	public <T> void update(T element) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.update(element);
		session.getTransaction().commit();
	}
	
	public <T> T getById(Class<T> cl, int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		T element = (T) session.get(cl, id);
		session.getTransaction().commit();
		return element;
	}

	public <T> void delete (T element){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(element);
		session.getTransaction().commit();
	}

	public <T extends Estate> Set<T> getAllEstatesForMakler(Class<T> cl, EstateAgent m) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(cl).createCriteria("agent")
				.add(Restrictions.eq("id", m.getId()));

		Set<T> results = new HashSet<T>(criteria.list());
		session.getTransaction().commit();
		return results;
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
			
			if(v.getApartment().getAgent().equals(m))
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
			
			if(k.getHouse().getAgent().equals(m))
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
			
			if(mv.getApartment().getAgent().getId() == m.getId())
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
			
			if(k.getHouse().getAgent().getId() == m.getId())
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
		h.setCity("Hamburg");
		h.setPostalCode(22527);
		h.setStreet("Vogt-Kölln-Straße");
		h.setStreetNumber("2a");
		h.setSquareArea(384);
		h.setFloors(5);
		h.setPrice(10000000);
		h.setGarden(true);
		h.setAgent(m);
		
		session.save(h);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session = sessionFactory.openSession();
		session.beginTransaction();
		EstateAgent m2 = (EstateAgent)session.get(EstateAgent.class, m.getId());
		Set<Estate> immos = m2.getEstates();
		Iterator<Estate> it = immos.iterator();
		
		while(it.hasNext()) {
			Estate i = it.next();
			System.out.println("Immo: "+i.getCity());
		}
		
		Apartment w = new Apartment();
		w.setCity("Hamburg");
		w.setPostalCode(22527);
		w.setStreet("Vogt-Kölln-Straße");
		w.setStreetNumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setBuiltinKitchen(true);
		w.setBalcony(false);
		w.setAgent(m);
		
		w = new Apartment();
		w.setCity("Berlin");
		w.setPostalCode(22527);
		w.setStreet("Vogt-Kölln-Straße");
		w.setStreetNumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setBuiltinKitchen(true);
		w.setBalcony(false);
		w.setAgent(m);

		session.beginTransaction();
		
		PurchaseContract kv = new PurchaseContract();
		kv.setHouse(h);
		kv.setContractingPerson(p1);
		kv.setContractNumber(9234);
		kv.setDate(new Date(System.currentTimeMillis()));
		kv.setPlace("Hamburg");
		kv.setNoOfInstallments(5);
		kv.setInterestRate(4);
		session.save(kv);
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
		session.save(mv);
		this.addTenancyContract(mv);
		
		session.getTransaction().commit();
		
		
		//close session
		session.close();
	}
}
