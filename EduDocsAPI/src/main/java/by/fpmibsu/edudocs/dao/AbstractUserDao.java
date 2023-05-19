package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Entity;
import by.fpmibsu.edudocs.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class AbstractUserDao<T extends User> extends AbstractDao<T> {
    public User findEntityByLogin(String login) throws DaoException {
        String sql = "SELECT * FROM Users WHERE login = ?";
        User user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user = new User(result.getString("login"),
                        result.getString("password"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getString("lastName"),
                        UUID.fromString(result.getString("id")));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }
}
