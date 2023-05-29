package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.TemplateDao;
import by.fpmibsu.edudocs.entities.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TemplateDaoImpl extends WrapperConnection implements TemplateDao {
    private static final Logger logger = LogManager.getLogger(TemplateDaoImpl.class);
    @Override
    public UUID create(Template entity) throws DaoException {
        String sql = "INSERT INTO Templates(id, name, route_to_document) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getRouteToDocument());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                statement.close();
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Templates`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Template> read() throws DaoException {
        String sql = "SELECT * FROM Templates";
        List<Template> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            while (result.next()) {
                Template user = new Template(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        result.getString("route_to_document"));
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
    public Template read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Templates WHERE id = ?";
        Template user = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            user = new Template(UUID.fromString(result.getString("id")),
                        result.getString("name"),
                        result.getString("route_to_document"));

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public void update(Template entity) throws DaoException {
        String sql = "UPDATE Templates SET id = ?, name = ?, route_to_document = ?";
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
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Templates WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identity.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
