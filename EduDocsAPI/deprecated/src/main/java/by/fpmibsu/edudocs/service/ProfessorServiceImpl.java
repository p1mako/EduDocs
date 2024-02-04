package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.interfaces.ProfessorDao;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.service.interfaces.ProfessorService;
import by.fpmibsu.edudocs.service.utils.AbstractService;

import java.util.UUID;

public class ProfessorServiceImpl extends AbstractService implements ProfessorService {
    @Override
    public Professor findByIdentity(UUID identity) throws DaoException {
        ProfessorDao dao = transaction.createDao(ProfessorDao.class);
        return dao.read(identity);
    }
}
