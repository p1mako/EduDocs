package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.User;

import java.util.UUID;

public interface StudentService extends Service {
    Student findByIdentity(UUID identity) throws DaoException;

}
