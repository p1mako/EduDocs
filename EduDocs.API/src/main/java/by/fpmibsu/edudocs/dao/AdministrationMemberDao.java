package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.AdministrationRole;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdministrationMemberDao extends AbstractUserDao {
    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM Admins";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        AdministrationRole[] administrationRoles = AdministrationRole.values();

        while (result.next()) {
            String id = result.getString("id");
            String sqlAdminDoc = "SELECT * FROM AdministrationDocuments Where administration_member = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlAdminDoc);
            statementAdminDoc.setString(1, id);
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
            ArrayList<Template> templates = new ArrayList<>();

            while (resultAdminDoc.next()) {
                String docId = resultAdminDoc.getString("template");
                TemplatesDao<Template> TD = new TemplatesDao<Template>();
                templates.add(TD.findEntityById(UUID.fromString(docId)));
            }
            statementAdminDoc.close();
            resultAdminDoc.close();

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id.toString());
            ResultSet resultUser = statementUser.executeQuery();

            User user = new AdministrationMember(UUID.fromString(id),
                    administrationRoles[result.getInt("role")],
                    Timestamp.valueOf(result.getString("assignment_start")),
                    Timestamp.valueOf(result.getString("assignment_end")),
                    resultUser.getString("login"),
                    resultUser.getString("password"),
                    resultUser.getString("name"),
                    resultUser.getString("surname"),
                    resultUser.getString("lastName"),
                    templates);

            resultUser.close();
            statementUser.close();
            users.add(user);
        }
        result.close();
        statement.close();
        return users;
    }

    @Override
    public User findEntityById(UUID id) throws SQLException {
        String sql = "SELECT * FROM Admins WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id.toString());
        ResultSet result = statement.executeQuery();
        AdministrationRole[] administrationRoles = AdministrationRole.values();

        String sqlAdminDoc = "SELECT * FROM AdministrationDocuments Where administration_member = ?";
        PreparedStatement statementAdminDoc = connection.prepareStatement(sqlAdminDoc);
        statementAdminDoc.setString(1, id.toString());
        ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
        ArrayList<Template> templates = new ArrayList<>();
        while (resultAdminDoc.next()) {
            String docId = resultAdminDoc.getString("template");
            TemplatesDao<Template> TD = new TemplatesDao<Template>();
            templates.add(TD.findEntityById(UUID.fromString(docId)));
        }
        statementAdminDoc.close();
        resultAdminDoc.close();

        String sqlUser = "SELECT * FROM Users Where id = ?";
        PreparedStatement statementUser = connection.prepareStatement(sqlUser);
        statementUser.setString(1, id.toString());
        ResultSet resultUser = statementUser.executeQuery();

        User user = new AdministrationMember(id,
                administrationRoles[result.getInt("role")],
                Timestamp.valueOf(result.getString("assignment_start")),
                Timestamp.valueOf(result.getString("assignment_end")),
                resultUser.getString("login"),
                resultUser.getString("password"),
                resultUser.getString("name"),
                resultUser.getString("surname"),
                resultUser.getString("lastName"),
                templates);

        resultUser.close();
        statementUser.close();
        result.close();
        statement.close();
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Admins WHERE id = ?";
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
        String sql = "INSERT INTO Admins(id, assignment_start, assignment_end, role) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        AdministrationMember admin = (AdministrationMember)entity;
        statement.setString(1, id.toString());
        statement.setString(2, admin.getFrom().toString());
        statement.setString(3, admin.getUntil().toString());
        statement.setString(4, AdministrationRole.valueOf(admin.getRole().toString()).toString());

        ArrayList<Template> templates = (ArrayList<Template>) admin.getAvailableTemplates();
        for(int i = 0; i < templates.size(); i++){
            String sqlTemp = "INSERT INTO AdministrationDocuments(administration_member, template) VALUES (?, ?)";
            PreparedStatement statementTemp = connection.prepareStatement(sqlTemp);
            statementTemp.setString(1, id.toString());
            statementTemp.setString(2, templates.get(i).getId().toString());
            statementTemp.executeUpdate();
            statementTemp.close();
        }
        statement.executeUpdate();
        statement.close();
        return true;
    }

    @Override
    public User update(User entity) throws SQLException {
        UUID id = entity.getId();
        String sql = "UPDATE Admins SET  assignment_start = ?,  assignment_end = ?, role = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        AdministrationMember admin = (AdministrationMember)entity;
        statement.setString(1, admin.getFrom().toString());
        statement.setString(2, admin.getUntil().toString());
        statement.setString(3, AdministrationRole.valueOf(admin.getRole().toString()).toString());
        statement.setString(4, id.toString());

        ArrayList<Template> templates = (ArrayList<Template>) admin.getAvailableTemplates();
        for(int i = 0; i < templates.size(); i++){
            String sqlTemp = "INSERT INTO AdministrationDocuments(administration_member, template) VALUES (?, ?)";
            PreparedStatement statementTemp = connection.prepareStatement(sqlTemp);
            statementTemp.setString(1, id.toString());
            statementTemp.setString(2, templates.get(i).getId().toString());
            statementTemp.executeUpdate();
            statementTemp.close();
        }
        statement.executeUpdate();
        statement.close();
        UserDao UD = new UserDao();
        UD.update(entity);
        return entity;
    }
}
