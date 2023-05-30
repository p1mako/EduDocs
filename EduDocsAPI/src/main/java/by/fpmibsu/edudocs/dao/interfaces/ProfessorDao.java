package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Request;

import java.util.List;

public interface ProfessorDao extends Dao<Professor> {
    List<Professor> read() throws DaoException;
}
