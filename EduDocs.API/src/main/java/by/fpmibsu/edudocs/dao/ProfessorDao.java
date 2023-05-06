package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Professor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfessorDao extends AbstractUserDao<Professor> {
    @Override
    public List<Professor> findAll() throws DaoException {
        String sql = "SELECT * FROM Professors";
        List<Professor> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String id = result.getString("id");
                String sqlUser = "SELECT * FROM Users Where id = ?";
                PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                statementUser.setString(1, id);
                ResultSet resultUser = statementUser.executeQuery();

                Professor user = new Professor(resultUser.getString("login"),
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public Professor findEntityById(UUID id) throws DaoException {
        String sql = "SELECT * FROM Professors";
        Professor user;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id.toString());
            ResultSet resultUser = statementUser.executeQuery();

            user = new Professor(resultUser.getString("login"),
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Professors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return setStatement(id, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean setStatement(UUID id, PreparedStatement preparedStatement) {
        PreparedStatement statement;
        try {
            statement = preparedStatement;
            statement.setString(1, id.toString());
            statement.executeUpdate();
            statement.close();
            UserDao ud = new UserDao();
            ud.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Professor entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Professor entity) throws DaoException {
        UserDao UD = new UserDao();
        try {
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Professors(id, degree) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public void update(Professor entity) throws DaoException {
        String sql = "UPDATE Professors SET degree = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            statement.close();
            UserDao UD = new UserDao();
            UD.update(entity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
