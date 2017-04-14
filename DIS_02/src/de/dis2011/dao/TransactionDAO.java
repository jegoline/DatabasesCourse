package de.dis2011.dao;

import java.util.ArrayList;

public interface TransactionDAO <Transaction> {
	boolean sign(Transaction t);
	ArrayList<Transaction> overview(int batchIndex, int batchSize);
}
