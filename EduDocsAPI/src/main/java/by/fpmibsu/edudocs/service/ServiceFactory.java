package by.fpmibsu.edudocs.service;


import by.fpmibsu.edudocs.dao.DaoException;

public interface ServiceFactory {
	<Type extends Service> Type getService(Class<Type> key) throws DaoException;

	void close();
}
