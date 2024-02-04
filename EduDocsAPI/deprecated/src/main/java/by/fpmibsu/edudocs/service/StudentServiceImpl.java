package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.interfaces.StudentDao;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.service.interfaces.StudentService;
import by.fpmibsu.edudocs.service.utils.AbstractService;

import java.util.UUID;

public class StudentServiceImpl extends AbstractService implements StudentService {
    @Override
    public Student findByIdentity(UUID identity) throws DaoException {
        StudentDao dao = transaction.createDao(StudentDao.class);
        return dao.read(identity);
    }
}
