package by.fpmibsu.edudocs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class WrapperConnection {
    protected Connection connection;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    static void setStatement(UUID id, PreparedStatement preparedStatement) throws DaoException {
        PreparedStatement statement;
        try {
            statement = preparedStatement;
            statement.setString(1, id.toString());
            statement.executeUpdate();
            statement.close();
            UserDaoImpl ud = new UserDaoImpl();
            ud.delete(id);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}