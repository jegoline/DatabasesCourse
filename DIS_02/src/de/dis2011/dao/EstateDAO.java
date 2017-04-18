package de.dis2011.dao;

import java.util.List;

import de.dis2011.data.Estate;

public interface EstateDAO<T extends Estate> extends EntryDAO<T> {
	List<T> loadAll();
}
