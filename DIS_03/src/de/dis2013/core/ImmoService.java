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
import de.dis2013.data.Contract;
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
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	public Set<Person> getAllPersons() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		List<Person> persons = session.createCriteria(Person.class).list();
		session.getTransaction().commit();
		
		return new HashSet<Person>(persons);
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
	
	
	public <T extends Contract> Set<T> getAllTenancyContractsForEstateAgent(Class<T> cl, EstateAgent m) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

//		Criteria criteria = session.createCriteria(cl).createCriteria("apartment")
//				.createCriteria("agent").add(Restrictions.eq("id", m.getId()));

		Criteria criteria = session.createCriteria(cl,"c");
		criteria.createAlias("contractingPerson","p");
		criteria.createAlias("apartment","e");
		criteria.createAlias("e.agent","a");
		criteria.add(Restrictions.eq("a.id", m.getId()));
		
		Set<T> results = new HashSet<T>(criteria.list());
		
		session.getTransaction().commit();
		return results;
	}
	
	public <T extends Contract> Set<T> getAllPurchaseContractsForEstateAgent(Class<T> cl, EstateAgent m) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();

//		Criteria criteria = session.createCriteria(cl).createCriteria("house")
//				.createCriteria("agent").add(Restrictions.eq("id", m.getId()));

		Criteria criteria = session.createCriteria(cl,"c");
		criteria.createAlias("contractingPerson","p");
		criteria.createAlias("house","e");
		criteria.createAlias("e.agent","a");
		criteria.add(Restrictions.eq("a.id", m.getId()));
		
		Set<T> results = new HashSet<T>(criteria.list());
		
		session.getTransaction().commit();
		return results;
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
		session.close();
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
		session.beginTransaction();
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
		
//		w = new Apartment();
//		w.setCity("Berlin");
//		w.setPostalCode(22527);
//		w.setStreet("Vogt-Kölln-Straße");
//		w.setStreetNumber("3");
//		w.setSquareArea(120);
//		w.setFloor(4);
//		w.setRent(790);
//		w.setBuiltinKitchen(true);
//		w.setBalcony(false);
//		w.setAgent(m);
		
		session.save(w);
		session.getTransaction().commit();
		
		
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
		
		session.getTransaction().commit();
		
		
		//close session
		session.close();
	}
}
