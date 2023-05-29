package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.ProfessorDao;
import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.dao.interfaces.UserDao;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfessorDaoImpl extends WrapperConnection implements ProfessorDao {

    private static final Logger logger = LogManager.getLogger(ProfessorDaoImpl.class);

    @Override
    public UUID create(Professor entity) throws DaoException {
        UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);
        try {
            userDao.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Professors(id, degree) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id.toString());
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                statement.close();
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Professors`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Professor> read() throws DaoException {
        String sql = "SELECT * FROM Professors";
        List<Professor> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                String id = result.getString("id");

                UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);
                User userpart = userDao.read(UUID.fromString(id));

                ArrayList<Request> requests = new ArrayList<>();

                RequestDaoImpl requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDaoImpl.class);
                requests = (ArrayList<Request>) requestDao.readByInitiator(UUID.fromString(id));

                Professor user = new Professor(
                        userpart.getLogin(),
                        userpart.getPassword(),
                        userpart.getName(),
                        userpart.getSurname(),
                        userpart.getLastName(),
                        UUID.fromString(id),
                        result.getString("degree"),
                        requests);
                users.add(user);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public Professor read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Professors";
        Professor user;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);
            User userpart = userDao.read(identity);

            List<Request> requests;

            RequestDaoImpl requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDaoImpl.class);
            requests = requestDao.readByInitiator(identity);

            user = new Professor(
                    userpart.getLogin(),
                    userpart.getPassword(),
                    userpart.getName(),
                    userpart.getSurname(),
                    userpart.getLastName(),
                    identity,
                    result.getString("degree"),
                    requests);

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public void update(Professor entity) throws DaoException {
        String sql = "UPDATE Professors SET degree = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            statement.close();
            UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);
            userDao.update(entity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Professors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatement(identity, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
