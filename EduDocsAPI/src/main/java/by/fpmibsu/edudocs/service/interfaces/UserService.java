package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService extends Service {
    List<User> findAll() throws DaoException;

    User findByIdentity(UUID identity) throws DaoException;

    User findByLogin(String login) throws DaoException;

    void save(User user) throws DaoException;

    void delete(UUID identity) throws DaoException;
}
