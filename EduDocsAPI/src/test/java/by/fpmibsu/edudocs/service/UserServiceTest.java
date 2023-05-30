package java.by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserServiceTest(){
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

    @org.junit.jupiter.api.Test
    void findAll() {
        UserService us;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            us = sf.getService(UserService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        ArrayList<User> userList = null;
        try {
            userList = (ArrayList<User>) us.findAll();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        assertFalse(userList.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void findByIdentity() {
        UserService us;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            us = sf.getService(UserService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            User user = us.findByIdentity(UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9"));
            User example = new User("fpm.Hartov", "o98,pluj68h7egv", "Stanislave", "Hartov", "Vitalievich", UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9"));
            assertEquals(user, example);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void findByLogin() {
        UserService us;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            us = sf.getService(UserService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            User user = us.findByLogin("fpm.Hartov");
            User example = new User("fpm.Hartov", "o98,pluj68h7egv", "Stanislave", "Hartov", "Vitalievich", UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9"));
            assertEquals(example, us.findByLogin("fpm.Hartov"));
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void save() {
        UserService us;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            us = sf.getService(UserService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            User example = new User("testLOG", "testP", "testN", "testS", "testL", UUID.randomUUID());
            us.save(example);
            assertEquals(example, us.findByLogin("testLOG"));
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void delete() {
        UserService us;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            us = sf.getService(UserService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            User example = new User("fpm.Hartov", "o98,pluj68h7egv", "Stanislave", "Hartov", "Vitalievich", UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9"));
            us.delete(example.getId());
            assertEquals(null, us.findByLogin("fpm.Hartov.ex"));
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}