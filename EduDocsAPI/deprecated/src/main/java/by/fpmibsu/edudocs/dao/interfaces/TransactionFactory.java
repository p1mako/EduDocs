package by.fpmibsu.edudocs.dao.interfaces;


import by.fpmibsu.edudocs.dao.DaoException;

public interface TransactionFactory {
	Transaction createTransaction() throws DaoException;

	void close();
}
