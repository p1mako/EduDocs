package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao extends AbstractDao<User> {
    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM Users";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
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
        return users;

    }

    @Override
    public User findEntityById(UUID id) throws SQLException{
        String sql = "SELECT * FROM Users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id.toString());
        ResultSet result = statement.executeQuery();
        User user = null;
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
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Users WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(User entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(User entity) throws SQLException {
        String sql = "INSERT INTO Users(login, password, name, surname, lastName) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, entity.getLogin());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getName());
        statement.setString(4, entity.getSurname());
        statement.setString(5, entity.getLastName());
        statement.executeUpdate();
        statement.close();
        return true;
    }

    @Override
    public User update(User entity) throws SQLException {
        String sql = "UPDATE Users SET login = ?, password = ?, name = ?, surname = ?, lastName = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, entity.getLogin());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getName());
        statement.setString(4, entity.getSurname());
        statement.setString(5, entity.getLastName());
        statement.setString(6, entity.getId().toString());
        statement.executeUpdate();
        statement.close();
        return entity;
    }
}
