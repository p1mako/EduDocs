package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Specialization;
import by.fpmibsu.edudocs.entities.Template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecializationDao extends AbstractDao<Specialization> {

    @Override
    public List<Specialization> findAll() throws DaoException {
        String sql = "SELECT * FROM Specialization";
        List<Specialization> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                Specialization user = new Specialization(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        Integer.parseInt(result.getString("registerNumber")));
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
    public Specialization findEntityById(UUID id) throws DaoException {
        String sql = "SELECT * FROM Specialization WHERE id = ?";
        Specialization user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user = new Specialization(id,
                        result.getString("name"),
                        Integer.parseInt(result.getString("registerNumber")));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Specialization WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Specialization entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Specialization entity) throws DaoException {
        String sql = "INSERT INTO Specialization(id, name, registerNumber) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName());
            statement.setString(3, String.valueOf(entity.getRegisterNumber()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public Specialization update(Specialization entity) throws DaoException {
        String sql = "UPDATE Specialization SET id = ?, name = ?, registerNumber = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName().toString());
            statement.setString(3, String.valueOf(entity.getRegisterNumber()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }
}
