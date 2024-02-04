package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.dao.interfaces.Transaction;
import by.fpmibsu.edudocs.dao.interfaces.TransactionFactory;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionFactoryImpl implements TransactionFactory {
	private static final Logger logger = LogManager.getLogger(TransactionFactoryImpl.class);
	private final Connection connection;
	
	public TransactionFactoryImpl() throws DaoException {
		connection = ConnectionPool.getInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch(SQLException e) {
			logger.error("It is impossible to turn off autocommiting for database connection", e);
			throw new DaoException(e);
		}
	}

	@Override
	public Transaction createTransaction() throws DaoException {
		return new TransactionImpl(connection);
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch(SQLException e) {}
	}
}
