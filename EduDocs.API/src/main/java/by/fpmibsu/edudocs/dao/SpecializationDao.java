package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Specialization;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SpecializationDao extends AbstractDao<Specialization> {

    @Override
    public List<Specialization> findAll() throws DaoException {
        return null;
    }

    @Override
    public Specialization findEntityById(UUID id) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public boolean delete(Specialization entity) {
        return false;
    }

    @Override
    public boolean create(Specialization entity) throws DaoException {
        return false;
    }

    @Override
    public Specialization update(Specialization entity) throws DaoException {
        return null;
    }
}
