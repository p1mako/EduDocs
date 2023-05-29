package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;

import java.util.List;

public interface RequestDao extends Dao<Request> {
    List<Request> read() throws DaoException;

    List<Request> getAllByTemplate(Template template) throws DaoException;
}
