package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Specialization;

import java.util.List;

public interface SpecializationDao extends Dao<Specialization> {
    List<Specialization> read() throws DaoException;
}
