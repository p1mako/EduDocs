package by.fpmibsu.edudocs.DAO;

import by.fpmibsu.edudocs.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao extends AbstractDao<User> {

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findEntityById(UUID id) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }

    @Override
    public boolean create(User entity) {
        return false;
    }

    @Override
    public User update(User entity) {
        return null;
    }
}
