package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.ProfessorDao;
import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfessorDaoImpl extends WrapperConnection implements ProfessorDao {

    private static final Logger logger = LogManager.getLogger(ProfessorDaoImpl.class);

    @Override
    public UUID create(Professor entity) throws DaoException {
        UserDaoImpl UD = new UserDaoImpl();
        try {
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Professors(id, degree) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id.toString());
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                statement.close();
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Professors`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Professor> read() throws DaoException {
        String sql = "SELECT * FROM Professors";
        List<Professor> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            while (result.next()) {
                String id = result.getString("id");
                String sqlUser = "SELECT * FROM Users Where id = ?";
                PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                statementUser.setString(1, id);
                ResultSet resultUser = statementUser.executeQuery();

                if (!resultUser.next()) {
                    resultUser.close();
                    statementUser.close();
                    return null;
                }

                String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
                PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
                statementAdminDoc.setString(1, id);
                ResultSet resultAdminDoc = statementAdminDoc.executeQuery();

                if (!resultAdminDoc.next()) {
                    resultAdminDoc.close();
                    statementAdminDoc.close();
                    return null;
                }

                ArrayList<Request> requests = new ArrayList<>();

                while (resultAdminDoc.next()) {
                    String docId = resultAdminDoc.getString("template");
                    RequestDao TD = new RequestDaoImpl();
                    requests.add(TD.read(UUID.fromString(docId)));
                }

                Professor user = new Professor(resultUser.getString("login"),
                        resultUser.getString("password"),
                        resultUser.getString("name"),
                        resultUser.getString("surname"),
                        resultUser.getString("lastName"),
                        UUID.fromString(id),
                        result.getString("degree"), requests);
                users.add(user);
                resultUser.close();
                statementUser.close();
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public Professor read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Professors";
        Professor user;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            if (!result.isBeforeFirst()){
                result.close();
                statement.close();
                return null;
            }

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, identity.toString());
            ResultSet resultUser = statementUser.executeQuery();

            if (!resultUser.next()) {
                resultUser.close();
                statementUser.close();
                return null;
            }

            if (!resultUser.isBeforeFirst()){
                resultUser.close();
                result.close();
                statement.close();
                return null;
            }

            String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
            statementAdminDoc.setString(1, identity.toString());
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();

            if (!resultAdminDoc.next()) {
                resultAdminDoc.close();
                statementAdminDoc.close();
                return null;
            }

            ArrayList<Request> requests = new ArrayList<>();

            while (resultAdminDoc.next()) {
                String docId = resultAdminDoc.getString("template");
                RequestDao TD = new RequestDaoImpl();
                requests.add(TD.read(UUID.fromString(docId)));
            }

            try {
                user = new Professor(resultUser.getString("login"),
                        resultUser.getString("password"),
                        resultUser.getString("name"),
                        resultUser.getString("surname"),
                        resultUser.getString("lastName"),
                        identity,
                        result.getString("degree"), requests);
            }
            catch (SQLException e){
                resultUser.close();
                statementUser.close();
                result.close();
                statement.close();
                return null;
            }
            resultUser.close();
            statementUser.close();
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public void update(Professor entity) throws DaoException {
        String sql = "UPDATE Professors SET degree = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getDegree());
            statement.executeUpdate();
            statement.close();
            UserDaoImpl UD = new UserDaoImpl();
            UD.update(entity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Professors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatement(identity, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
