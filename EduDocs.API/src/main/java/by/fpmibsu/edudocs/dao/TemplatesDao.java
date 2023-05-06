package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TemplatesDao extends AbstractDao<Template> {
    @Override
    public List<Template> findAll() throws DaoException {
        String sql = "SELECT * FROM Templates";
        List<Template> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                Template user = new Template(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        result.getString("route"));
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
    public Template findEntityById(UUID id) throws DaoException {
        String sql = "SELECT * FROM Templates WHERE id = ?";
        Template user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user = new Template(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        result.getString("route"));
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
        String sql = "DELETE FROM Templates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Template entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Template entity) throws DaoException {
        String sql = "INSERT INTO Templates(id, name, route) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getRouteToDocument());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public void update(Template entity) throws DaoException {
        String sql = "UPDATE Templates SET id = ?, name = ?, route = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName().toString());
            statement.setString(3, entity.getRouteToDocument());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
