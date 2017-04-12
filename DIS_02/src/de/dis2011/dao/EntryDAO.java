package de.dis2011.dao;

public interface EntryDAO<T> {
	boolean insert(T entry);
	boolean update(T entry);
	boolean delete(T entry);
	T load(int id);
}
