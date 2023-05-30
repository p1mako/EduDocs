package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.*;
import by.fpmibsu.edudocs.entities.*;
import by.fpmibsu.edudocs.entities.utils.AdministrationRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdministrationMemberDaoImpl extends WrapperConnection implements AdministrationMemberDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    @Override
    public UUID create(AdministrationMember entity) throws DaoException {
        try {
           var resultSet = insertToAdmins(entity);
           if (resultSet.next()) {
               return UUID.fromString(resultSet.getString(1));
           } else {
            logger.error("There is no autoincremented index after trying to add record into table `Admins`");
            throw new DaoException();
        }
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

            TemplateDao templateDao = new TransactionFactoryImpl().createTransaction().createDao(TemplateDao.class);

            UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);

            RequestDao requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDao.class);

            while (result.next()) {
                String id = result.getString("id");

                List<Template> templates = templateDao.getAvailableTemplates(UUID.fromString(id));

                User userpart = userDao.read(UUID.fromString(id));

                List<Request> requests = new ArrayList<>();

                for(var template : templates){
                    List<Request> requestListpart;
                    requestListpart = requestDao.getAllByTemplate(template);
                    if(requestListpart != null){
                        requests.add((Request) requestListpart);
                    }
                }

                AdministrationMember user = new AdministrationMember(UUID.fromString(id),
                        administrationRoles[result.getInt("role")],
                        Timestamp.valueOf(result.getString("assignment_start")),
                        Timestamp.valueOf(result.getString("assignment_end")),
                        userpart.getLogin(),
                        userpart.getPassword(),
                        userpart.getName(),
                        userpart.getSurname(),
                        userpart.getLastName(),
                        templates,
                        requests);

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

            if (!result.next()) {
                result.close();
                statement.close();
                return null;
            }

            AdministrationRole[] administrationRoles = AdministrationRole.values();

            TemplateDao templateDao = new TransactionFactoryImpl().createTransaction().createDao(TemplateDao.class);

            UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);

            RequestDao requestDao = new TransactionFactoryImpl().createTransaction().createDao(RequestDao.class);

            List<Template> templates = templateDao.getAvailableTemplates(identity);

            User userpart = userDao.read(identity);

            List<Request> requests = new ArrayList<>();

            for(var template : templates){
                List<Request> requestListpart;
                requestListpart = requestDao.getAllByTemplate(template);
                if(requestListpart != null){
                    requests.addAll(requestListpart);
                }
            }

            var stringAsignementEnd = result.getString("assignment_end");
            Timestamp asignementEnd = null;
            if (stringAsignementEnd != null) {
                asignementEnd = Timestamp.valueOf(stringAsignementEnd);
            }

            user = new AdministrationMember(identity,
                    administrationRoles[result.getInt("role")],
                    Timestamp.valueOf(result.getString("assignment_start")),
                    asignementEnd,
                    userpart.getLogin(),
                    userpart.getPassword(),
                    userpart.getName(),
                    userpart.getSurname(),
                    userpart.getLastName(),
                    templates, requests);

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
            insertToAdmins(entity);
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

    private ResultSet insertToAdmins(AdministrationMember entity) throws DaoException, SQLException {
        UserDao userDao = new TransactionFactoryImpl().createTransaction().createDao(UserDao.class);
        userDao.create(entity);
        UUID id = entity.getId();
        String sql = "INSERT INTO Admins(id, assignment_start, assignment_end, role) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id.toString());
        statement.setString(2, entity.getFrom().toString());
        statement.setString(3, entity.getUntil().toString());
        statement.setString(4, AdministrationRole.valueOf(entity.getRole().toString()).toString());
        statement.executeUpdate();

        ArrayList<Template> templates = (ArrayList<Template>) entity.getAvailableTemplates();

        insertToAdministrationDocuments(id, statement, templates);

        var resultSet = statement.getGeneratedKeys();
        statement.close();
        return resultSet;
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
