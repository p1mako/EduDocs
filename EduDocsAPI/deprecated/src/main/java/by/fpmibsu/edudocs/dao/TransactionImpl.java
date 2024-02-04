package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionImpl implements Transaction {
    private static final Logger logger = LogManager.getLogger(TransactionImpl.class);

    private static final Map<Class<? extends Dao<?>>, Class<? extends WrapperConnection>> classes = new ConcurrentHashMap<>();

    static {
        classes.put(AdministrationMemberDao.class, AdministrationMemberDaoImpl.class);
        classes.put(DocumentDao.class, DocumentDaoImpl.class);
        classes.put(ProfessorDao.class, ProfessorDaoImpl.class);
        classes.put(RequestDao.class, RequestDaoImpl.class);
        classes.put(SpecializationDao.class, SpecializationDaoImpl.class);
        classes.put(StudentDao.class, StudentDaoImpl.class);
        classes.put(TemplateDao.class, TemplateDaoImpl.class);
        classes.put(UserDao.class, UserDaoImpl.class);
    }

    private final Connection connection;

    public TransactionImpl(Connection connection) {
        this.connection = connection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Type extends Dao<?>> Type createDao(Class<Type> key) throws DaoException {
        Class<? extends WrapperConnection> value = classes.get(key);
        if (value != null) {
            try {
                WrapperConnection dao = value.getDeclaredConstructor().newInstance();
                dao.setConnection(connection);
                return (Type) dao;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.error("It is impossible to create data access object", e);
                throw new DaoException(e);
            }
        }
        return null;
    }

    @Override
    public void commit() throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error("It is impossible to commit transaction", e);
            throw new DaoException(e);
        }
    }

    @Override
    public void rollback() throws DaoException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error("It is impossible to rollback transaction", e);
            throw new DaoException(e);
        }
    }
}
