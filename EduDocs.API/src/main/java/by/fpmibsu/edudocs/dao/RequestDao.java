package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestDao extends AbstractDao<Request> {

    final String SQL_GET_ALL = "SELECT * FROM Requests";
    final String SQL_GET_BY_UwU = "SELECT * FROM Requests WHERE id = ?";
    final String SQL_DELETE_BY_Uwu = "DELETE FROM Requests WHERE id = ?";
    final String SQL_INSERT_REQUEST = "INSERT INTO Requests(id, status, template, initiator, created, document) VALUES (?, ?, ?, ?, ?, ?)";
    final String SQL_UPDATE_REQUEST = "UPDATE Requests SET id = ?, status = ?, template = ?, initiator = ?, created = ?, document = ?)";

    @Override
    public List<Request> findAll() throws DaoException {
        DocumentsDao documentsDao = new DocumentsDao();
        TemplatesDao templatesDao = new TemplatesDao();
        UserDao userDao = new UserDao();
        List<Request> requests = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SQL_GET_ALL);
            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString("id"));
                RequestStatus status = RequestStatus.values()[result.getInt("status")];
                Template template = templatesDao.findEntityById(UUID.fromString(result.getString("template")));
                User initiator = userDao.findEntityById(UUID.fromString(result.getString("initiator")));
                Timestamp created = result.getTimestamp("created");
                Document document = documentsDao.findEntityById(UUID.fromString(result.getString("document")));

                Request user = new Request(uuid, status, template, initiator, created, document);
                requests.add(user);
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return requests;
    }

    @Override
    public Request findEntityById(UUID id) throws DaoException {
        DocumentsDao documentsDao = new DocumentsDao();
        TemplatesDao templatesDao = new TemplatesDao();
        UserDao userDao = new UserDao();
        Request request;
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_UwU);
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();

            RequestStatus status = RequestStatus.values()[result.getInt("status")];
            Template template = templatesDao.findEntityById(UUID.fromString(result.getString("template")));
            User initiator = userDao.findEntityById(UUID.fromString(result.getString("initiator")));
            Timestamp created = result.getTimestamp("created");
            Document document = documentsDao.findEntityById(UUID.fromString(result.getString("document")));

            request = new Request(id, status, template, initiator, created, document);

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return request;
    }

    @Override
    public boolean delete(UUID id) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_Uwu);
            statement.setString(1, id.toString());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Request entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Request entity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REQUEST);
            preparedStatement.setString(1, entity.getId().toString());
            preparedStatement.setString(2, entity.getStatus().toString());
            preparedStatement.setString(3, entity.getTemplate().toString());
            preparedStatement.setString(4, entity.getInitiator().toString());
            preparedStatement.setString(5, entity.getCreated().toString());
            preparedStatement.setString(6, entity.getDocument().toString());
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Request update(Request entity) throws DaoException{
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_REQUEST);
            preparedStatement.setString(1, entity.getId().toString());
            preparedStatement.setString(2, entity.getStatus().toString());
            preparedStatement.setString(3, entity.getTemplate().toString());
            preparedStatement.setString(4, entity.getInitiator().toString());
            preparedStatement.setString(5, entity.getCreated().toString());
            preparedStatement.setString(6, entity.getDocument().toString());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }
}
