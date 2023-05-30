package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Entity;

import java.util.UUID;

public interface Dao<Type extends Entity> {
    UUID create(Type entity) throws DaoException;

    Type read(UUID identity) throws DaoException;

    void update(Type entity) throws DaoException;

    void delete(UUID identity) throws DaoException;
}
