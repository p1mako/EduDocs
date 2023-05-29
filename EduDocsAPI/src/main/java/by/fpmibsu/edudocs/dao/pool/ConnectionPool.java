package by.fpmibsu.edudocs.dao.pool;

import org.apache.logging.log4j.LogManager;

import by.fpmibsu.edudocs.dao.DaoException;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

final public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private String url;

    private Properties prop;
    private int maxSize;
    private int checkConnectionTimeout;

    private final BlockingQueue<PooledConnection> freeConnections = new LinkedBlockingQueue<>();
    private final Set<PooledConnection> usedConnections = new ConcurrentSkipListSet<>();

    private ConnectionPool() {

    }

    public synchronized Connection getConnection() throws DaoException {
        PooledConnection connection = null;
        while (connection == null) {
            try {
                if (!freeConnections.isEmpty()) {
                    connection = freeConnections.take();
                    if (!connection.isValid(checkConnectionTimeout)) {
                        try {
                            connection.getConnection().close();
                        } catch (SQLException ignored) {
                        }
                        connection = null;
                    }
                } else if (usedConnections.size() < maxSize) {
                    connection = createConnection();
                } else {
                    logger.error("The limit of number of database connections is exceeded");
                    throw new DaoException();
                }
            } catch (InterruptedException | SQLException e) {
                logger.error("It is impossible to connect to a database", e);
                throw new DaoException(e);
            }
        }
        usedConnections.add(connection);
        logger.debug(String.format("Connection was received from pool. Current pool size: %d used connections; %d free connection", usedConnections.size(), freeConnections.size()));
        return connection;
    }

    synchronized void freeConnection(PooledConnection connection) {
        try {
            if (connection.isValid(checkConnectionTimeout)) {
                connection.clearWarnings();
                connection.setAutoCommit(true);
                usedConnections.remove(connection);
                freeConnections.put(connection);
                logger.debug(String.format("Connection was returned into pool. Current pool size: %d used connections; %d free connection", usedConnections.size(), freeConnections.size()));
            }
        } catch (SQLException | InterruptedException e1) {
            logger.warn("It is impossible to return database connection into pool", e1);
            try {
                connection.getConnection().close();
            } catch (SQLException ignored) {
            }
        }
    }

    public synchronized void init(String driverClass, String url, Properties prop, int startSize, int maxSize, int checkConnectionTimeout) throws DaoException {
        try {
            destroy();
            Class.forName(driverClass);
            this.url = url;
            this.prop = prop;
            this.maxSize = maxSize;
            this.checkConnectionTimeout = checkConnectionTimeout;
            for (int counter = 0; counter < startSize; counter++) {
                freeConnections.put(createConnection());
            }
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            logger.fatal("It is impossible to initialize connection pool", e);
            throw new DaoException(e);
        }
    }

    private static final ConnectionPool instance = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return instance;
    }

    private PooledConnection createConnection() throws SQLException {
        return new PooledConnection(DriverManager.getConnection(url, prop));
    }

    public synchronized void destroy() {
        usedConnections.addAll(freeConnections);
        freeConnections.clear();
        for (PooledConnection connection : usedConnections) {
            try {
                connection.getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        usedConnections.clear();
    }
}
