package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Template;

import java.util.List;

public interface TemplateDao extends Dao<Template> {
    List<Template> read() throws DaoException;
}
