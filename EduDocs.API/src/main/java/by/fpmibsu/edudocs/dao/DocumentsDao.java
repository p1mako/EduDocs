package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentsDao extends AbstractDao<Document> {

    final String SQL_GET_ALL = "SELECT * FROM Documents";
    @Override
    public List<Document> findAll() throws DaoException {
        AdministrationMemberDao administrationMemberDao = new AdministrationMemberDao();
        TemplatesDao templatesDao = new TemplatesDao();
        UserDao userDao = new UserDao();
        List<Document> documents = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_GET_ALL);
            Document document;
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("author"));
                Template template = templatesDao.findEntityById(UUID.fromString(result.getString("template")));
                Timestamp created = result.getTimestamp("created");
                Date date = result.getDate("date");
                AdministrationMember administrationMember = (AdministrationMember) administrationMemberDao.findEntityById(uuid);
                User initiator = userDao.findEntityById(UUID.fromString(result.getString("initiator")));
                document = new Document(uuid, template, created, date, administrationMember, initiator);
                documents.add(document);
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return documents;
    }

    @Override
    public Document findEntityById(UUID id) {
        Document document = null;
        String sql = "SELECT * FROM documents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                document = new Document(
                        UUID.fromString(resultSet.getString("id")), null,
                        resultSet.getTimestamp("created"),
                        resultSet.getDate("valid_through"),
                        null, //author
                        null //initiator//template
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = false;
        String sql = "DELETE FROM Documents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            int rows = statement.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Document entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Document entity) {
        boolean result = false;
        String sql = "INSERT INTO documents (id, created, valid_through) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId().toString());
            statement.setTimestamp(2, entity.getCreated());
            statement.setDate(3, entity.getValidThrough());
            int rows = statement.executeUpdate();
            result = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Document update(Document entity) throws DaoException {
        if (entity.getId() == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE documents SET template_id = ?, created = ?, valid_through = ?, author_id = ?, initiator_id = ? WHERE id = ?");
            statement.setString(1, entity.getTemplate().getId().toString());
            statement.setString(2, entity.getCreated().toString());
            statement.setDate(3, entity.getValidThrough());
            statement.setObject(4, entity.getAuthor() != null ? entity.getAuthor().getId() : null);
            statement.setObject(5, entity.getInitiator().getId());
            statement.setObject(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }
}
