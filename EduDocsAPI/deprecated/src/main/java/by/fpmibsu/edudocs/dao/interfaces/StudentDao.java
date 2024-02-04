package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Student;

import java.util.List;

public interface StudentDao extends Dao<Student> {
    List<Student> read() throws DaoException;
}
