package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Template;

import java.util.List;

public interface TemplateService extends Service {

    List<Template> getAll() throws DaoException;
}
