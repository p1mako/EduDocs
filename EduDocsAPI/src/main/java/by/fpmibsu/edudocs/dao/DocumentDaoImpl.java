package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.DocumentDao;
import by.fpmibsu.edudocs.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentDaoImpl extends WrapperConnection implements DocumentDao {
    final String SQL_GET_ALL = "SELECT * FROM Documents";

    @Override
    public boolean create(Document entity) throws DaoException {
        String sql = "INSERT INTO documents (id, created, valid_through) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId().toString());
            statement.setTimestamp(2, entity.getCreated());
            statement.setDate(3, entity.getValidThrough());
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Document> read() throws DaoException {
        AdministrationMemberDaoImpl administrationMemberDaoImpl = new AdministrationMemberDaoImpl();
        TemplateDaoImpl templateDao = new TemplateDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        List<Document> documents = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_GET_ALL);
            Document document;
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("author"));
                Template template = templateDao.read(UUID.fromString(result.getString("template")));
                Timestamp created = result.getTimestamp("created");
                Date date = result.getDate("date");
                AdministrationMember administrationMember = administrationMemberDaoImpl.read(uuid);
                User initiator = userDao.read(UUID.fromString(result.getString("initiator")));
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
    public Document read(UUID identity) throws DaoException {
        TemplateDaoImpl templateDao = new TemplateDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        AdministrationMemberDaoImpl administrationMemberDaoImpl = new AdministrationMemberDaoImpl();
        Document document;
        String sql = "SELECT * FROM Documents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();
            Template template = templateDao.read(UUID.fromString(result.getString("template")));
            Timestamp created = result.getTimestamp("created");
            Date date = result.getDate("date");
            AdministrationMember administrationMember = administrationMemberDaoImpl.read(identity);
            User initiator = userDao.read(identity);
            document = new Document(identity, template, created, date, administrationMember, initiator);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return document;
    }

    @Override
    public void update(Document entity) throws DaoException {
        if (entity.getId() == null) {
            return;
        }
        try {
            String sql = "UPDATE Documents SET template = ?, created = ?, valid_through = ?, author = ?, initiator = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
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

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Documents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identity.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


}
