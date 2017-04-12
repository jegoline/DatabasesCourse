package de.dis2011.dao;

import de.dis2011.data.Estate;

public class EstateDAO implements EntryDAO<Estate>{
	
	@Override
	public Estate load (int id) {
		return null;
	}

	@Override
	public boolean insert(Estate entry) {
		return false;
	}

	@Override
	public boolean update(Estate entry) {
		return false;
	}

	@Override
	public boolean delete(Estate entry) {
		return false;
	}

}
