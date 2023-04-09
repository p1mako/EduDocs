package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.User;

import java.util.List;
import java.util.UUID;

public class RequestDao<T extends User> extends AbstractDao<T>  {
    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public T findEntityById(UUID id) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public boolean delete(T entity) {
        return false;
    }

    @Override
    public boolean create(T entity) {
        return false;
    }

    @Override
    public T update(T entity) {
        return null;
    }
}
