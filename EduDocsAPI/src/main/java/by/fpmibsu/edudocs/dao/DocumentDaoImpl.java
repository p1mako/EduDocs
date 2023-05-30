package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.AdministrationMemberDao;
import by.fpmibsu.edudocs.dao.interfaces.DocumentDao;
import by.fpmibsu.edudocs.dao.interfaces.TemplateDao;
import by.fpmibsu.edudocs.dao.interfaces.UserDao;
import by.fpmibsu.edudocs.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.FactoryConfigurationError;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentDaoImpl extends WrapperConnection implements DocumentDao {
    private static final Logger logger = LogManager.getLogger(DocumentDaoImpl.class);
    final String SQL_GET_ALL = "SELECT * FROM Documents";

    @Override
    public UUID create(Document entity) throws DaoException {
        if (entity == null) {
            return null;
        }
        String sql = "INSERT INTO documents (id, created, valid_through) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getId().toString());
            statement.setTimestamp(2, entity.getCreated());
            statement.setDate(3, entity.getValidThrough());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            statement.close();
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Documents`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Document> read() throws DaoException {
        AdministrationMemberDaoImpl administrationMemberDaoImpl = new TransactionFactoryImpl().createTransaction().createDao(AdministrationMemberDaoImpl.class);
        TemplateDao templateDao = new TransactionFactoryImpl().createTransaction().createDao(TemplateDao.class);
        UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);

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
        AdministrationMemberDao administrationMemberDaoImpl = new TransactionFactoryImpl().createTransaction().createDao(AdministrationMemberDao.class);
        TemplateDao templateDao = new TransactionFactoryImpl().createTransaction().createDao(TemplateDao.class);
        UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);

        Document document;
        String sql = "SELECT * FROM Documents WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

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
