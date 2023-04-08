package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.User;

import java.util.List;
import java.util.UUID;

public class ProfessorDao extends AbstractUserDao{
    @Override
    public List<User> findAll() {
        //Add sql
        return null;
    }

    @Override
    public User findEntityById(UUID id) {
        //TODO add sql
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        //TODO add sql
        //else return false;
        return true;
    }

    @Override
    public boolean delete(User entity) {
        //TODO add sql
        //else return false;
        return true;
    }

    @Override
    public boolean create(User entity) {
        //TODO add sql
        //else return false;
        return true;
    }

    @Override
    public User update(User entity) {
        //TODO add sql
        return entity;
    }
}
