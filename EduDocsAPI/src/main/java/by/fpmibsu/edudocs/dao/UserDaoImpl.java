package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.dao.interfaces.UserDao;
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

public class UserDaoImpl extends WrapperConnection implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    @Override
    public UUID create(User entity) throws DaoException {
        String sql = "INSERT INTO Users(login, password, name, surname, lastName) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insertToUsers(statement, entity, sql);
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                statement.close();
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Users`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void insertToUsers(PreparedStatement statement, User entity, String sql) throws SQLException {
        statement.setString(1, entity.getLogin());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getName());
        statement.setString(4, entity.getSurname());
        statement.setString(5, entity.getLastName());
    }

    @Override
    public List<User> read() throws DaoException {
        String sql = "SELECT * FROM Users";
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                User user = new User(result.getString("login"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getString("lastName"),
                        UUID.fromString(result.getString("id")));
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
    public User read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();

            String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
            statementAdminDoc.setString(1, identity.toString());
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
            ArrayList<Request> requests = new ArrayList<>();

            while (resultAdminDoc.next()) {
                String docId = resultAdminDoc.getString("template");
                RequestDao TD = new RequestDaoImpl();
                requests.add(TD.read(UUID.fromString(docId)));
            }

            if (result.next()) {
                user = new User(result.getString("login"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getString("lastName"),
                        UUID.fromString(result.getString("id")), requests);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public void update(User entity) throws DaoException {
        String sql = "UPDATE Users SET login = ?, password = ?, name = ?, surname = ?, lastName = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            insertToUsers(statement, entity, sql);
            statement.setString(6, entity.getId().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Users WHERE id = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public User findUserByLogin(String login) throws DaoException {
        String sql = "SELECT * FROM Users WHERE login = ?";
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();

            UUID identity = UUID.fromString(result.getString("id"));

            String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
            statementAdminDoc.setString(1, identity.toString());
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
            ArrayList<Request> requests = new ArrayList<>();

            while (resultAdminDoc.next()) {
                String docId = resultAdminDoc.getString("template");
                RequestDao TD = new RequestDaoImpl();
                requests.add(TD.read(UUID.fromString(docId)));
            }


            if (result.next()) {
                user = new User(result.getString("login"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getString("lastName"),
                        identity, requests);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }
}
