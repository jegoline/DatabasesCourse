package dao;


import java.util.List;

public interface TransactionDAO <Transaction> {
	List<Transaction> loadAll();
}
