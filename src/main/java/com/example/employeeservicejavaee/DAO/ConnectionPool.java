package com.example.employeeservicejavaee.DAO;


import com.example.employeeservicejavaee.DAO.exceptions.ConnectionPoolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private final static ConnectionPool INSTANCE = new ConnectionPool();
    private static final Logger logger = LogManager.getLogger();
    private final String DRIVER_NAME = "driverName";
    private final String PROPERTY_FILE_NAME = "db.properties";
    private final String URL_PROPERTY = "url";
    private final String USER_PROPERTY = "user";
    private final String PASSWORD_PROPERTY = "password";
    private final String POOL_SIZE_PROPERTY = "poolSize";
    private final int DEFAULT_POOL_SIZE = 5;
    private final String driverName;
    private final String url;
    private final String user;
    private final String password;
    private int poolSize;
    private final BlockingQueue<Connection> givenConnections;
    private final BlockingQueue<Connection> freeConnections;
    private final Lock lock = new ReentrantLock();

    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    private ConnectionPool() {
        //ResourceBundle bundle = ResourceBundle.getBundle("db.properties", new Locale("en", "US"));
        InputStream a = this.getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
        Properties properties = new Properties();
        try {
            properties.load(a);
        } catch (IOException e) {
            logger.error("Error loading property files for database. Name for the file should be \"db.properties\"", e);
            throw new ConnectionPoolException("Error loading property files for database. Name for the file should be \"db.properties\"", e);
        }
        this.driverName = properties.getProperty(DRIVER_NAME);
        this.url = properties.getProperty(URL_PROPERTY);
        this.user = properties.getProperty(USER_PROPERTY);
        this.password = properties.getProperty(PASSWORD_PROPERTY);
        try {
            this.poolSize = Integer.parseInt(properties.getProperty(POOL_SIZE_PROPERTY));
        } catch (NumberFormatException e) {
            logger.debug("Poolsize was set to default value");
            this.poolSize = DEFAULT_POOL_SIZE;
        }

        try {
            Class.forName(driverName);
            givenConnections = new ArrayBlockingQueue<>(poolSize);
            freeConnections = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                freeConnections.add(connection);
            }
        } catch (SQLException e) {
            logger.error("SQLException in ConnectionPool", e);
            throw new ConnectionPoolException("SQLException in ConnectionPool", e);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find database driver class", e);
            throw new ConnectionPoolException("Can't find database driver class", e);
        }
    }

    public void dispose() {
        clearConnectionQueue();
    }

    private void clearConnectionQueue() {
        try {
            closeConnectionsQueue(givenConnections);
            closeConnectionsQueue(freeConnections);
        } catch (SQLException e) {
            logger.error("Error clearing connection queues", e);
        }
    }
    public Connection takeConnection() throws ConnectionPoolException {
        Connection connection;
        try {
            connection = freeConnections.take();
            givenConnections.add(connection);
        } catch (InterruptedException e) {
            logger.error("Error connecting to the data source.", e);
            throw new ConnectionPoolException("Error connecting to the data source.", e);
        }
        return connection;
    }

    public void returnConnection(Connection connection) {
        lock.lock();
        givenConnections.remove(connection);
        freeConnections.add(connection);
        lock.unlock();
    }

    private void closeConnectionsQueue(BlockingQueue<Connection> queue)
            throws SQLException {
        Connection connection;
        while ((connection = queue.poll()) != null) {
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            connection.close();
        }
    }
}