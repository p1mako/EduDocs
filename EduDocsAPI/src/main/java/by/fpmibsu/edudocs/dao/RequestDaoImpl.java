package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.*;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestDaoImpl extends WrapperConnection implements RequestDao {
    private static final Logger logger = LogManager.getLogger(RequestDaoImpl.class);

    final String SQL_GET_ALL = "SELECT * FROM Requests";
    final String SQL_GET_BY_UwU = "SELECT * FROM Requests WHERE id = ?";
    final String SQL_DELETE_BY_Uwu = "DELETE FROM Requests WHERE id = ?";
    final String SQL_INSERT_REQUEST = "INSERT INTO Requests(id, status, template, initiator, created, document) VALUES (?, ?, ?, ?, ?, ?)";
    final String SQL_UPDATE_REQUEST = "UPDATE Requests SET id = ?, status = ?, template = ?, initiator = ?, created = ?, document = ?";

    @Override
    public UUID create(Request entity) throws DaoException {
        try {
            var resultSet = changeRequests(entity, SQL_INSERT_REQUEST);
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Requests`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Request> read() throws DaoException {

        List<Request> requests = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_GET_ALL);

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("id"));
                requests.add(makeRequest(result, uuid));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return requests;
    }

    @Override
    public Request read(UUID identity) throws DaoException {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_UwU);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            Request request = makeRequest(result, identity);
            result.close();
            statement.close();
            return request;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Request entity) throws DaoException {
        try {
            changeRequests(entity, SQL_UPDATE_REQUEST);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_Uwu);
            statement.setString(1, identity.toString());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private ResultSet changeRequests(Request entity, String sqlUpdateRequest) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlUpdateRequest, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getId().toString());
        statement.setString(2, entity.getStatus().toString());
        statement.setString(3, entity.getTemplate().toString());
        statement.setString(4, entity.getInitiator().toString());
        statement.setString(5, entity.getCreated().toString());
        statement.setString(6, entity.getDocument().toString());
        var resultSet = statement.getGeneratedKeys();
        statement.close();
        return resultSet;
    }

    private Request makeRequest(ResultSet result, UUID uuid) throws SQLException, DaoException {
        var factory = new TransactionFactoryImpl();
        DocumentDao documentsDaoImpl = factory.createTransaction().createDao(DocumentDao.class);
        TemplateDao templateDao = factory.createTransaction().createDao(TemplateDao.class);
        UserDao userDao = factory.createTransaction().createDao(UserDao.class);
        RequestStatus status = null;
        Template template = null;
        User initiator = null;
        Timestamp created = null;
        Document document = null;

            status = RequestStatus.values()[result.getInt("status")];
            template = templateDao.read(UUID.fromString(result.getString("template")));
            initiator = userDao.read(UUID.fromString(result.getString("initiator")));
            created = result.getTimestamp("created");
            var documentString = result.getString("document");
            if (documentString != null){
                document = documentsDaoImpl.read(UUID.fromString(documentString));
            }
        return new Request(uuid, status, template, initiator, created, document);
    }
}
