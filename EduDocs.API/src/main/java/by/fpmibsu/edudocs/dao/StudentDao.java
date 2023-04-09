package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Specialization;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDao extends AbstractUserDao {
    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM Students";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        StudentStatus[] statuses = StudentStatus.values();
        while (result.next()) {

            String id = result.getString("id");
            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id.toString());
            ResultSet resultUser = statementUser.executeQuery();

            String idSpec = result.getString("specialization");
            String sqlSpec = "SELECT * Specializations Users Where id = ?";
            PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
            statementUser.setString(1, idSpec.toString());
            ResultSet resultSpec = statementUser.executeQuery();

            User user = new Student(UUID.fromString(id),
                    resultUser.getString("login"),
                    resultUser.getString("password"),
                    resultUser.getString("name"),
                    resultUser.getString("surname"),
                    resultUser.getString("lastName"),
                    result.getTimestamp("entryDate"),
                    result.getInt("group"),
                    result.getInt("uniqueNumber"),
                    statuses[result.getInt("status")],
                    new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getInt("registerNumber"))
            );
            resultSpec.close();
            resultUser.close();
            statementUser.close();
            statementSpec.close();
            users.add(user);
        }
        result.close();
        statement.close();
        return users;
    }

    @Override
    public User findEntityById(UUID id) throws SQLException {
        String sql = "SELECT * FROM Students  WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id.toString());
        ResultSet result = statement.executeQuery();
        StudentStatus[] statuses = StudentStatus.values();

        String sqlUser = "SELECT * FROM Users Where id = ?";
        PreparedStatement statementUser = connection.prepareStatement(sqlUser);
        statementUser.setString(1, id.toString());
        ResultSet resultUser = statementUser.executeQuery();

        String idSpec = result.getString("specialization");
        String sqlSpec = "SELECT * Specializations Users Where id = ?";
        PreparedStatement statementSpec = connection.prepareStatement(sqlSpec);
        statementUser.setString(1, idSpec.toString());
        ResultSet resultSpec = statementUser.executeQuery();

        User user = new Student(id,
                resultUser.getString("login"),
                resultUser.getString("password"),
                resultUser.getString("name"),
                resultUser.getString("surname"),
                resultUser.getString("lastName"),
                result.getTimestamp("entryDate"),
                result.getInt("group"),
                result.getInt("uniqueNumber"),
                statuses[result.getInt("status")],
                new Specialization(UUID.fromString(idSpec), resultSpec.getString("name"), resultSpec.getInt("registerNumber"))
        );

        resultSpec.close();
        resultUser.close();
        statementUser.close();
        statementSpec.close();
        result.close();
        statement.close();
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Students WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.executeUpdate();
            statement.close();
            UserDao UD = new UserDao();
            UD.delete(id);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(User entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(User entity) throws SQLException {
        UserDao UD = new UserDao();
        UD.create(entity);
        UUID id = entity.getId();
        String sql = "INSERT INTO Users(group_num, status, entry_date, uniqueNumber, specialization, id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        Student student = (Student)entity;
        statement.setString(1, String.valueOf(student.getGroup()));
        statement.setString(2, String.valueOf(StudentStatus.valueOf(String.valueOf(student.getStatus()))));
        statement.setString(3, student.getEntryDate().toString());
        statement.setString(4, String.valueOf(student.getUniqueNumber()));
        statement.setString(5, student.getSpecialization().getId().toString());
        statement.setString(6, id.toString());
        statement.executeUpdate();
        statement.close();
        return true;
    }

    @Override
    public User update(User entity) throws SQLException {
        String sql = "UPDATE Student SET group_num = ?, status = ?, entry_date = ?, uniqueNumber = ?, specialization = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        Student student = (Student)entity;
        statement.setString(1, String.valueOf(student.getGroup()));
        statement.setString(2, String.valueOf(StudentStatus.valueOf(String.valueOf(student.getStatus()))));
        statement.setString(3, student.getEntryDate().toString());
        statement.setString(4, String.valueOf(student.getUniqueNumber()));
        statement.setString(5, student.getSpecialization().getId().toString());
        statement.executeUpdate();
        statement.close();
        UserDao UD = new UserDao();
        UD.update(entity);
        return entity;
    }
}
