package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.AdministrationRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdministrationMemberDao extends AbstractUserDao<AdministrationMember> {
    @Override
    public List<AdministrationMember> findAll() throws DaoException {
        String sql = "SELECT * FROM Admins";
        List<AdministrationMember> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            AdministrationRole[] administrationRoles = AdministrationRole.values();

            while (result.next()) {
                String id = result.getString("id");
                String sqlAdminDoc = "SELECT * FROM AdministrationDocuments Where administration_member = ?";
                PreparedStatement statementAdminDoc = connection.prepareStatement(sqlAdminDoc);
                statementAdminDoc.setString(1, id);
                ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
                List<Template> templates = new ArrayList<>();

                while (resultAdminDoc.next()) {
                    String docId = resultAdminDoc.getString("template");
                    TemplatesDao TD = new TemplatesDao();
                    templates.add(TD.findEntityById(UUID.fromString(docId)));
                }
                statementAdminDoc.close();
                resultAdminDoc.close();

                String sqlUser = "SELECT * FROM Users Where id = ?";
                PreparedStatement statementUser = connection.prepareStatement(sqlUser);
                statementUser.setString(1, id);
                ResultSet resultUser = statementUser.executeQuery();

                AdministrationMember user = new AdministrationMember(UUID.fromString(id),
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public AdministrationMember findEntityById(UUID id) throws DaoException {
        String sql = "SELECT * FROM Admins WHERE id = ?";
        AdministrationMember user;
        try {
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
                TemplatesDao TD = new TemplatesDao();
                templates.add(TD.findEntityById(UUID.fromString(docId)));
            }
            statementAdminDoc.close();
            resultAdminDoc.close();

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, id.toString());
            ResultSet resultUser = statementUser.executeQuery();

            user = new AdministrationMember(id,
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        String sql = "DELETE FROM Admins WHERE id = ?";
        PreparedStatement statement;
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
    public boolean delete(AdministrationMember entity) {
        return delete(entity.getId());
    }

    @Override
    public boolean create(AdministrationMember entity) throws DaoException {
        try {
            UserDao UD = new UserDao();
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Admins(id, assignment_start, assignment_end, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            AdministrationMember admin = (AdministrationMember) entity;
            statement.setString(1, id.toString());
            statement.setString(2, admin.getFrom().toString());
            statement.setString(3, admin.getUntil().toString());
            statement.setString(4, AdministrationRole.valueOf(admin.getRole().toString()).toString());

            ArrayList<Template> templates = (ArrayList<Template>) admin.getAvailableTemplates();
            for (Template template : templates) {
                String sqlTemp = "INSERT INTO AdministrationDocuments(administration_member, template) VALUES (?, ?)";
                PreparedStatement statementTemp = connection.prepareStatement(sqlTemp);
                statementTemp.setString(1, id.toString());
                statementTemp.setString(2, template.getId().toString());
                statementTemp.executeUpdate();
                statementTemp.close();
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return true;
    }

    @Override
    public AdministrationMember update(AdministrationMember entity) throws DaoException {
        try {
            UUID id = entity.getId();
            String sql = "UPDATE Admins SET  assignment_start = ?,  assignment_end = ?, role = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            AdministrationMember admin = (AdministrationMember) entity;
            statement.setString(1, admin.getFrom().toString());
            statement.setString(2, admin.getUntil().toString());
            statement.setString(3, AdministrationRole.valueOf(admin.getRole().toString()).toString());
            statement.setString(4, id.toString());

            List<Template> templates = admin.getAvailableTemplates();
            for (Template template : templates) {
                String sqlTemp = "INSERT INTO AdministrationDocuments(administration_member, template) VALUES (?, ?)";
                PreparedStatement statementTemp = connection.prepareStatement(sqlTemp);
                statementTemp.setString(1, id.toString());
                statementTemp.setString(2, template.getId().toString());
                statementTemp.executeUpdate();
                statementTemp.close();
            }
            statement.executeUpdate();
            statement.close();
            UserDao UD = new UserDao();
            UD.update(entity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }
}
