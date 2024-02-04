package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.interfaces.TemplateDao;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.service.interfaces.TemplateService;
import by.fpmibsu.edudocs.service.utils.AbstractService;

import java.util.List;

public class TemplateServiceImpl extends AbstractService implements TemplateService {
    @Override
    public List<Template> getAll() throws DaoException {
        TemplateDao templateDao = transaction.createDao(TemplateDao.class);
        return templateDao.read();
    }
}
