package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Specialization;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.fpmibsu.edudocs.dao.ProfessorDao.setStatement;

public class StudentDao extends AbstractUserDao<Student> {
    @Override
    public List<Student> findAll() throws DaoException {
        String sql = "SELECT * FROM Students";
        List<Student> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            StudentStatus[] statuses = StudentStatus.values();
            while (result.next()) {

                String id = result.getString("id");
                String sqlUser = "SELECT * FROM Users Where id = ?";
                PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                statementUser.setString(1, id);
                ResultSet resultUser = statementUser.executeQuery();

                String idSpec = result.getString("specialization");
                String sqlSpec = "SELECT * FROM Specializations Where id = ?";
                PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
                statementUser.setString(1, idSpec);
                ResultSet resultSpec = statementUser.executeQuery();

                Student user = new Student(UUID.fromString(id),
                        resultUser.getString("login"),
                        resultUser.getString("password"),
                        resultUser.getString("name"),
                        resultUser.getString("surname"),
                        resultUser.getString("lastName"),
                        result.getTimestamp("entryDate"),
                        result.getInt("group"),
                        result.getInt("uniqueNumber"),
                        statuses[result.getInt("status")],
                        new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getString("registerNumber"))
                );
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
    public Student findEntityById(UUID id) throws DaoException {
        String sql = "SELECT * FROM Students  WHERE id = ?";
        Student student;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            StudentStatus[] statuses = StudentStatus.values();

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id.toString());
            ResultSet resultUser = statementUser.executeQuery();

            String idSpec = result.getString("specialization");
            String sqlSpec = "SELECT * FROM Specializations Where id = ?";
            PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
            statementUser.setString(1, idSpec);
            ResultSet resultSpec = statementUser.executeQuery();

            student = new Student(id,
                    resultUser.getString("login"),
                    resultUser.getString("password"),
                    resultUser.getString("name"),
                    resultUser.getString("surname"),
                    resultUser.getString("lastName"),
                    result.getTimestamp("entryDate"),
                    result.getInt("group"),
                    result.getInt("uniqueNumber"),
                    statuses[result.getInt("status")],
                    new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getString("registerNumber"))
            );

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
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            return setStatement(id, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Student entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(Student student) throws DaoException {
        UserDao UD = new UserDao();
        try {
            UD.create(student);
            UUID id = student.getId();
            String sql = "INSERT INTO Students(group_num, status, entry_date, uniqueNumber, specialization, id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);;
            statement.setString(1, String.valueOf(student.getGroup()));
            statement.setString(2, String.valueOf(StudentStatus.valueOf(String.valueOf(student.getStatus()))));
            statement.setString(3, student.getEntryDate().toString());
            statement.setString(4, String.valueOf(student.getUniqueNumber()));
            statement.setString(5, student.getSpecialization().getId().toString());
            statement.setString(6, id.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public boolean update(Student entity) throws DaoException {
        String sql = "UPDATE Students SET group_num = ?, status = ?, entry_date = ?, uniqueNumber = ?, specialization = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(entity.getGroup()));
            statement.setString(2, String.valueOf(StudentStatus.valueOf(String.valueOf(entity.getStatus()))));
            statement.setString(3, entity.getEntryDate().toString());
            statement.setString(4, String.valueOf(entity.getUniqueNumber()));
            statement.setString(5, entity.getSpecialization().getId().toString());
            statement.executeUpdate();
            statement.close();
            UserDao UD = new UserDao();
            UD.update(entity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return false;
    }
}
