package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;

import java.util.List;

public interface UserDao extends Dao<User> {
    List<User> read() throws DaoException;
}
