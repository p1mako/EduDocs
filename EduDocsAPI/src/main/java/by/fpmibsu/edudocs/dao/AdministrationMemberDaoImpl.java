package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.AdministrationMemberDao;
import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.AdministrationRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdministrationMemberDaoImpl extends WrapperConnection implements AdministrationMemberDao {
    @Override
    public boolean create(AdministrationMember entity) throws DaoException {
        try {
            UserDaoImpl UD = new UserDaoImpl();
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Admins(id, assignment_start, assignment_end, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.setString(2, entity.getFrom().toString());
            statement.setString(3, entity.getUntil().toString());
            statement.setString(4, AdministrationRole.valueOf(entity.getRole().toString()).toString());

            ArrayList<Template> templates = (ArrayList<Template>) entity.getAvailableTemplates();
            insertToAdministrationDocuments(id, statement, templates);
            int rows = statement.executeUpdate();
            statement.close();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<AdministrationMember> read() throws DaoException {
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
                    TemplateDaoImpl TD = new TemplateDaoImpl();
                    templates.add(TD.read(UUID.fromString(docId)));
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
    public AdministrationMember read(UUID identity) throws DaoException {
        String sql = "SELECT * FROM Admins WHERE id = ?";
        AdministrationMember user;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, identity.toString());
            ResultSet result = statement.executeQuery();
            AdministrationRole[] administrationRoles = AdministrationRole.values();

            String sqlAdminDoc = "SELECT * FROM AdministrationDocuments Where administration_member = ?";
            PreparedStatement statementAdminDoc = connection.prepareStatement(sqlAdminDoc);
            statementAdminDoc.setString(1, identity.toString());
            ResultSet resultAdminDoc = statementAdminDoc.executeQuery();
            ArrayList<Template> templates = new ArrayList<>();
            while (resultAdminDoc.next()) {
                String docId = resultAdminDoc.getString("template");
                TemplateDaoImpl TD = new TemplateDaoImpl();
                templates.add(TD.read(UUID.fromString(docId)));
            }
            statementAdminDoc.close();
            resultAdminDoc.close();

            String sqlUser = "SELECT * FROM Users Where id = ?";
            PreparedStatement statementUser = connection.prepareStatement(sqlUser);
            statementUser.setString(1, identity.toString());
            ResultSet resultUser = statementUser.executeQuery();

            user = new AdministrationMember(identity,
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
    public void update(AdministrationMember entity) throws DaoException {
        try {
            UserDaoImpl UD = new UserDaoImpl();
            UD.create(entity);
            UUID id = entity.getId();
            String sql = "INSERT INTO Admins(id, assignment_start, assignment_end, role) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id.toString());
            statement.setString(2, entity.getFrom().toString());
            statement.setString(3, entity.getUntil().toString());
            statement.setString(4, AdministrationRole.valueOf(entity.getRole().toString()).toString());

            ArrayList<Template> templates = (ArrayList<Template>) entity.getAvailableTemplates();
            insertToAdministrationDocuments(id, statement, templates);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(UUID identity) throws DaoException {
        String sql = "DELETE FROM Admins WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setStatement(identity, statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void insertToAdministrationDocuments(UUID id, PreparedStatement statement, List<Template> templates) throws SQLException {
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
    }
}
