package by.fpmibsu.edudocs.service.utils;


import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.service.interfaces.Service;

public interface ServiceFactory {
	<Type extends Service> Type getService(Class<Type> key) throws DaoException;

	void close();
}
