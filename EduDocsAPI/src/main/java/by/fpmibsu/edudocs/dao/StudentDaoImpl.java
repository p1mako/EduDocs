package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.dao.interfaces.StudentDao;
import by.fpmibsu.edudocs.dao.interfaces.UserDao;
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
        UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);

        try {
            userDao.create(entity);
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

            UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);
            while (result.next()) {
                String id;
                try {
                    id = result.getString("id");
                } catch (SQLException e) {
                    return null;
                }

                User userpart = userDao.read(UUID.fromString(id));

                String idSpec;
                try {
                    idSpec = result.getString("specialization");
                } catch (SQLException e) {
                    return null;
                }

                SpecializationDaoImpl specializationDao = new TransactionFactoryImpl().createTransaction().createDao(SpecializationDaoImpl.class);
                Specialization specialization = specializationDao.read(UUID.fromString(idSpec));


                RequestDaoImpl requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDaoImpl.class);
                ArrayList<Request> requests = (ArrayList<Request>) requestDao.readByInitiator(UUID.fromString(id));

                Student user = new Student(UUID.fromString(id),
                        userpart.getLogin(),
                        userpart.getPassword(),
                        userpart.getName(),
                        userpart.getSurname(),
                        userpart.getLastName(),
                        result.getTimestamp("entryDate"),
                        result.getInt("group"),
                        result.getInt("uniqueNumber"),
                        statuses[result.getInt("status") - 1],
                        specialization,
                        requests
                );
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

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            UserDaoImpl userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDaoImpl.class);
            User userpart = userDao.read(identity);

            String idSpec;
            try {
                idSpec = result.getString("specialization");
            } catch (SQLException e) {
                return null;
            }

            SpecializationDaoImpl specializationDao = new TransactionFactoryImpl().createTransaction().createDao(SpecializationDaoImpl.class);
            Specialization specialization = specializationDao.read(UUID.fromString(idSpec));


            RequestDaoImpl requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDaoImpl.class);
            List<Request> requests = requestDao.readByInitiator(identity);

            student = new Student(identity,
                    userpart.getLogin(),
                    userpart.getPassword(),
                    userpart.getName(),
                    userpart.getSurname(),
                    userpart.getLastName(),
                    result.getTimestamp("entryDate"),
                    result.getInt("group"),
                    result.getInt("uniqueNumber"),
                    statuses[result.getInt("status") - 1],
                    specialization,
                    requests
            );

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
            UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);
            userDao.update(entity);
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
