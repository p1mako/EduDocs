package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;

public interface Transaction {
	<Type extends Dao<?>> Type createDao(Class<Type> key) throws DaoException;

	void commit() throws DaoException;

	void rollback() throws DaoException;
}