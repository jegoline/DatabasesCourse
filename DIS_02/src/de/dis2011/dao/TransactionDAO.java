package de.dis2011.dao;


import java.util.List;

public interface TransactionDAO <Transaction> {
	boolean sign(Transaction t);
	List<Transaction> loadAll();
}
