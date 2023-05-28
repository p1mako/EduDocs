package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceImplTest {

    RequestServiceImplTest(){
        String url = "jdbc:sqlserver://localhost";
        Properties prop = new Properties();
        InputStream stream = App.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            ConnectionPool.getInstance().init("com.microsoft.sqlserver.jdbc.SQLServerDriver",url, prop, 10, 100, 300);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getRequestStatusFromDataSource() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}