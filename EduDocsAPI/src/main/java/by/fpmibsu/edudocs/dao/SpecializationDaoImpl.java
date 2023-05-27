package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.SpecializationDao;
import by.fpmibsu.edudocs.entities.Specialization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecializationDaoImpl extends WrapperConnection implements SpecializationDao {


    @Override
    public boolean create(Specialization entity) throws DaoException {
        String sql = "INSERT INTO Specializations(id, name, registerNumber) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName());
            statement.setString(3, String.valueOf(entity.getRegisterNumber()));
            int rows = statement.executeUpdate();
            statement.close();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Specialization> read() throws DaoException {
        String sql = "SELECT * FROM Specializations";
        List<Specialization> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                Specialization user = new Specialization(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        result.getString("registerNumber"));
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
    public Specialization read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Specializations WHERE id = ?";
        Specialization user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user = new Specialization(identity,
                        result.getString("name"),
                        result.getString("registerNumber"));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public void update(Specialization entity) throws DaoException {

    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Specializations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identity.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }
}
