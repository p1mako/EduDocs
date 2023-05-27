package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestDaoImpl extends WrapperConnection implements RequestDao {

    final String SQL_GET_ALL = "SELECT * FROM Requests";
    final String SQL_GET_BY_UwU = "SELECT * FROM Requests WHERE id = ?";
    final String SQL_DELETE_BY_Uwu = "DELETE FROM Requests WHERE id = ?";
    final String SQL_INSERT_REQUEST = "INSERT INTO Requests(id, status, template, initiator, created, document) VALUES (?, ?, ?, ?, ?, ?)";
    final String SQL_UPDATE_REQUEST = "UPDATE Requests SET id = ?, status = ?, template = ?, initiator = ?, created = ?, document = ?";

    @Override
    public boolean create(Request entity) throws DaoException {
        try {
            changeRequests(entity, SQL_INSERT_REQUEST);
            return true;
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

    private void changeRequests(Request entity, String sqlUpdateRequest) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateRequest);
        preparedStatement.setString(1, entity.getId().toString());
        preparedStatement.setString(2, entity.getStatus().toString());
        preparedStatement.setString(3, entity.getTemplate().toString());
        preparedStatement.setString(4, entity.getInitiator().toString());
        preparedStatement.setString(5, entity.getCreated().toString());
        preparedStatement.setString(6, entity.getDocument().toString());
    }

    private Request makeRequest(ResultSet result, UUID uuid) throws SQLException, DaoException {
        DocumentDaoImpl documentsDaoImpl = new DocumentDaoImpl();
        TemplateDaoImpl templateDao = new TemplateDaoImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        RequestStatus status = RequestStatus.values()[result.getInt("status")];
        Template template = templateDao.read(UUID.fromString(result.getString("template")));
        User initiator = userDao.read(UUID.fromString(result.getString("initiator")));
        Timestamp created = result.getTimestamp("created");
        Document document = documentsDaoImpl.read(UUID.fromString(result.getString("document")));
        return new Request(uuid, status, template, initiator, created, document);
    }
}
