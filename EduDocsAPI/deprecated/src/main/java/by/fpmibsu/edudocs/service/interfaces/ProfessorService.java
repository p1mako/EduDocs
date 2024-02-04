package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Student;

import java.util.UUID;

public interface ProfessorService extends Service {
    Professor findByIdentity(UUID identity) throws DaoException;
}
