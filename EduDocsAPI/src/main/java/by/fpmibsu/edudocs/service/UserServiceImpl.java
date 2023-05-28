package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.interfaces.UserDao;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;

import java.util.List;
import java.util.UUID;

import static org.apache.logging.log4j.core.util.NameUtil.md5;

public class UserServiceImpl extends AbstractService implements UserService {
    @Override
    public List<User> findAll() throws DaoException {
        UserDao dao = transaction.createDao(UserDao.class);
        return dao.read();
    }

    @Override
    public User findByIdentity(UUID identity) throws DaoException {
        UserDao dao = transaction.createDao(UserDao.class);
        return dao.read(identity);
    }

    @Override
    public User findByLogin(String login) throws DaoException {
        UserDao dao = transaction.createDao(UserDao.class);
        return dao.findUserByLogin(login);
    }

    @Override
    public void save(User user) throws DaoException {
        UserDao dao = transaction.createDao(UserDao.class);
        if(user.getId() != null) {
            if(user.getPassword() != null) {
                user.setPassword(md5(user.getPassword()));
            } else {
                User oldUser = dao.read(user.getId());
                user.setPassword(oldUser.getPassword());
            }
            dao.update(user);
        } else {
            user.setPassword(md5(""));
            user.setId(dao.create(user));
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        UserDao dao = transaction.createDao(UserDao.class);
        dao.delete(identity);
    }
}
