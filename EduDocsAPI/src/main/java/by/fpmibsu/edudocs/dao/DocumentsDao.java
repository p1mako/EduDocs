package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.*;

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
                AdministrationMember administrationMember = administrationMemberDao.findEntityById(uuid);
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
    public Document findEntityById(UUID id) throws DaoException {
        TemplatesDao templatesDao = new TemplatesDao();
        UserDao userDao = new UserDao();
        AdministrationMemberDao administrationMemberDao = new AdministrationMemberDao();
        Document document;
        String sql = "SELECT * FROM Documents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            Template template = templatesDao.findEntityById(UUID.fromString(result.getString("template")));
            Timestamp created = result.getTimestamp("created");
            Date date = result.getDate("date");
            AdministrationMember administrationMember = administrationMemberDao.findEntityById(id);
            User initiator = userDao.findEntityById(id);
            document = new Document(id, template, created, date, administrationMember, initiator);
        } catch (SQLException e) {
            throw new DaoException(e);
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
    public void update(Document entity) throws DaoException {
        if (entity.getId() == null) {
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Documents SET template = ?, created = ?, valid_through = ?, author = ?, initiator = ? WHERE id = ?");
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
    }
}
