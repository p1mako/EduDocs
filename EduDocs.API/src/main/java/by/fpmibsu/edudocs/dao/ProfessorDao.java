package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfessorDao extends AbstractUserDao {
    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM Professors";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (result.next()) {
            String id = result.getString("id");
            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id);
            ResultSet resultUser = statementUser.executeQuery();

            User user = new Professor(resultUser.getString("login"),
                    resultUser.getString("password"),
                    resultUser.getString("name"),
                    resultUser.getString("surname"),
                    resultUser.getString("lastName"),
                    UUID.fromString(id),
                    result.getString("degree"));
            users.add(user);
            resultUser.close();
            statementUser.close();
        }
        result.close();
        statement.close();
        return users;
    }

    @Override
    public User findEntityById(UUID id) throws SQLException {
        String sql = "SELECT * FROM Professors";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        String sqlUser = "SELECT * FROM Users Where id = ?";
        PreparedStatement statementUser = connection.prepareStatement(sqlUser);
        statementUser.setString(1, id.toString());
        ResultSet resultUser = statementUser.executeQuery();

        User user = new Professor(resultUser.getString("login"),
                resultUser.getString("password"),
                resultUser.getString("name"),
                resultUser.getString("surname"),
                resultUser.getString("lastName"),
                id,
                result.getString("degree"));
        resultUser.close();
        statementUser.close();
        result.close();
        statement.close();
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Professors WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();
            statement.close();
            UserDao UD = new UserDao();
            UD.delete(id);
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
        UserDao UD = new UserDao();
        UD.create(entity);
        UUID id = entity.getId();
        String sql = "INSERT INTO Professors(id, degree) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        Professor professor = (Professor) entity;
        statement.setString(1, id.toString());
        statement.setString(1, professor.getDegree());
        statement.executeUpdate();
        statement.close();
        return true;
    }

    @Override
    public User update(User entity) throws SQLException {
        String sql = "UPDATE Professors SET degree = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        Professor professor = (Professor) entity;
        statement.setString(1, professor.getDegree());
        statement.executeUpdate();
        statement.close();
        UserDao UD = new UserDao();
        UD.update(entity);
        return entity;
    }
}
