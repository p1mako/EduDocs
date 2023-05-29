package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.dao.interfaces.StudentDao;
import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;
import jdk.jshell.Snippet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDaoImpl extends WrapperConnection implements StudentDao {

    private static final Logger logger = LogManager.getLogger(StudentDaoImpl.class);

    @Override
    public UUID create(Student entity) throws DaoException {
        UserDaoImpl UD = new UserDaoImpl();
        try {
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Students(group_num, status, entry_date, uniqueNumber, specialization, id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            updateStudents(entity, statement);
            statement.setString(6, id.toString());
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                statement.close();
                return UUID.fromString(resultSet.getString(1));
            } else {
                logger.error("There is no autoincremented index after trying to add record into table `Specializations`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Student> read() throws DaoException {
        String sql = "SELECT * FROM Students";
        List<Student> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            StudentStatus[] statuses = StudentStatus.values();

            if (!result.isBeforeFirst()) {
                result.close();
                statement.close();
                return null;
            }

            while (result.next()) {
                String id;
                try {
                    id = result.getString("id");
                } catch (SQLException e) {
                    return null;
                }
                String sqlUser = "SELECT * FROM Users Where id = ?";
                PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                statementUser.setString(1, id);
                ResultSet resultUser = statementUser.executeQuery();

                if (!resultUser.isBeforeFirst()) {
                    resultUser.close();
                    statementUser.close();
                    return null;
                }
                String idSpec;
                try {
                    idSpec = result.getString("specialization");
                } catch (SQLException e) {
                    return null;
                }
                String sqlSpec = "SELECT * FROM Specializations Where id = ?";
                PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
                statementUser.setString(1, idSpec);
                ResultSet resultSpec = statementUser.executeQuery();

                if (!resultSpec.isBeforeFirst()) {
                    resultSpec.close();
                    statementSpec.close();
                    return null;
                }

                String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
                PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
                statementAdminDoc.setString(1, id);
                ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
                ArrayList<Request> requests = new ArrayList<>();

                if (!resultAdminDoc.isBeforeFirst()) {
                    resultAdminDoc.close();
                    statementAdminDoc.close();
                    return null;
                }

                while (resultAdminDoc.next()) {
                    String docId = resultAdminDoc.getString("template");
                    RequestDao TD = new RequestDaoImpl();
                    requests.add(TD.read(UUID.fromString(docId)));
                }

                String login;
                String password;
                String name;
                String surname;
                String lastname;
                Timestamp entryDate;
                int group;
                int uniqid;
                StudentStatus status;
                Specialization specialization;

                try {
                    login = resultUser.getString("login");
                    password = resultUser.getString("password");
                    name = resultUser.getString("name");
                    surname = resultUser.getString("surname");
                    lastname = resultUser.getString("lastName");
                    entryDate = result.getTimestamp("entryDate");
                    group = result.getInt("group");
                    uniqid = result.getInt("uniqueNumber");
                    status = statuses[result.getInt("status")];
                    specialization = new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getString("registerNumber"));

                } catch (SQLException e) {
                    return null;
                }


                Student user = new Student(UUID.fromString(id), login, password, name, surname, lastname, entryDate, group, uniqid, status, specialization, requests);

                resultSpec.close();
                resultUser.close();
                statementUser.close();
                statementSpec.close();
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
    public Student read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Students  WHERE id = ?";
        Student student;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();
            StudentStatus[] statuses = StudentStatus.values();

            if (!result.isBeforeFirst()) {
                result.close();
                statement.close();
                return null;
            }

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, identity.toString());
            ResultSet resultUser = statementUser.executeQuery();

            if (!resultUser.isBeforeFirst()) {
                resultUser.close();
                statementUser.close();
                return null;
            }
            String idSpec;

            try {
                idSpec = result.getString("specialization");
            } catch (SQLException e){
                return null;
            }
            String sqlSpec = "SELECT * FROM Specializations Where id = ?";
            PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
            statementUser.setString(1, idSpec);
            ResultSet resultSpec = statementUser.executeQuery();

            if (!resultSpec.isBeforeFirst()) {
                resultSpec.close();
                statementSpec.close();
                return null;
            }


            String sqlStudentRequest = "SELECT * FROM Requests Where initiator = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlStudentRequest);
            statementAdminDoc.setString(1, identity.toString());
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
            ArrayList<Request> requests = new ArrayList<>();

            if (!resultAdminDoc.isBeforeFirst()) {
                resultAdminDoc.close();
                statementAdminDoc.close();
                return null;
            }

            while (resultAdminDoc.next()) {
                String docId;
                try{docId = resultAdminDoc.getString("template");}
                catch (SQLException e){
                    return null;
                }
                RequestDao TD = new RequestDaoImpl();
                requests.add(TD.read(UUID.fromString(docId)));
            }

            statementAdminDoc.close();
            resultAdminDoc.close();


            String login;
            String password;
            String name;
            String surname;
            String lastname;
            Timestamp entryDate;
            int group;
            int uniqid;
            StudentStatus status;
            Specialization specialization;

            try {
                login = resultUser.getString("login");
                password = resultUser.getString("password");
                name = resultUser.getString("name");
                surname = resultUser.getString("surname");
                lastname = resultUser.getString("lastName");
                entryDate = result.getTimestamp("entryDate");
                group = result.getInt("group");
                uniqid = result.getInt("uniqueNumber");
                status = statuses[result.getInt("status")];
                specialization = new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getString("registerNumber"));

            } catch (SQLException e) {
                return null;
            }

            student = new Student(identity, login, password, name, surname, lastname, entryDate, group, uniqid, status, specialization, requests);

            resultSpec.close();
            resultUser.close();
            statementUser.close();
            statementSpec.close();

            resultSpec.close();
            resultUser.close();
            statementUser.close();
            statementSpec.close();
            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return student;
    }

    @Override
    public void update(Student entity) throws DaoException {
        String sql = "UPDATE Students SET group_num = ?, status = ?, entry_date = ?, uniqueNumber = ?, specialization = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            updateStudents(entity, statement);
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
        String sql = "DELETE FROM Students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatement(identity, statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void updateStudents(Student entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, String.valueOf(entity.getGroup()));
        statement.setString(2, String.valueOf(StudentStatus.valueOf(String.valueOf(entity.getStatus()))));
        statement.setString(3, entity.getEntryDate().toString());
        statement.setString(4, String.valueOf(entity.getUniqueNumber()));
        statement.setString(5, entity.getSpecialization().getId().toString());
    }
}
