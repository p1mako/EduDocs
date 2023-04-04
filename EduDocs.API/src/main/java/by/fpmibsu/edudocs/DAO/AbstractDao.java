package by.fpmibsu.edudocs.DAO;

import by.fpmibsu.edudocs.entities.User;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public abstract class AbstractDao<T extends User> {
    protected Connection connection;

    public abstract List<T> findAll();

    public abstract T findEntityById(UUID id);

    public abstract boolean delete(UUID id);

    public abstract boolean delete(T entity);

    public abstract boolean create(T entity);

    public abstract T update(T entity);

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // log
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}