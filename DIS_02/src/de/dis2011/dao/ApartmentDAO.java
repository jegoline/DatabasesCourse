package de.dis2011.dao;

import de.dis2011.data.Apartment;

public class ApartmentDAO implements EntryDAO<Apartment>{
	
	@Override
	public Apartment load (int id) {
		return null;
	}
	
	@Override
	public boolean insert(Apartment entry) {
		return false;
	}

	@Override
	public boolean update(Apartment entry) {
		return false;
	}

	@Override
	public boolean delete(Apartment entry) {
		return false;
	}
}
