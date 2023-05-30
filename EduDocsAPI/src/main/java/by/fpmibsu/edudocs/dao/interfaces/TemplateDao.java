package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Template;

import java.util.List;
import java.util.UUID;

public interface TemplateDao extends Dao<Template> {
    List<Template> read() throws DaoException;

    List<Template> getAvailableTemplates(UUID member) throws DaoException;
}
