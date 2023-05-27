package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Entity;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDao<T extends Entity> {
    protected Connection connection;

    public abstract List<T> findAll() throws DaoException;

    public abstract T findEntityById(UUID id) throws DaoException;

    public abstract boolean delete(UUID id);

    public abstract boolean delete(T entity);

    public abstract boolean create(T entity) throws DaoException;

    public abstract boolean update(T entity) throws DaoException;

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //log
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}