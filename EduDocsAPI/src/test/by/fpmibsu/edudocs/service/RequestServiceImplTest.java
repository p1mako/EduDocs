package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceImplTest {

    private User user = new User("fpm.Leschik", "o98,pluj68h7egv", "Dmitry", "Leschik", "Anatolievich",UUID.fromString("22FED5A9-B35F-4D4D-92C0-FF50C78C5DA7"));

    private Template template = new Template(null, "test", "testRoute");

    private Document document = new Document(null, template, Timestamp.valueOf(LocalDateTime.now()), null, null, user);
    private Request request = new Request(UUID.fromString("94C96E03-7448-43CE-BD35-563A441384D5"), RequestStatus.Sent, template, user, Timestamp.valueOf("1900-01-01 00:00:00.0000000"), null);

    RequestServiceImplTest() {
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
            ConnectionPool.getInstance().init("com.microsoft.sqlserver.jdbc.SQLServerDriver", url, prop, 10, 100, 300);
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
    @Test
    void createRequest() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        assertTrue(rs.createRequest(request));
    }

    @Test
    void getAllRequests() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Request> list = null;
        list = (ArrayList<Request>) rs.getAllRequests();
        assertFalse(rs.getAllRequests().isEmpty());
    }

    @Test
    void updateRequestStatus() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        assertTrue(rs.updateRequestStatus(request, RequestStatus.BeingProcessed));
    }

    @Test
    void deleteRequestByAdmin() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        assertTrue(rs.deleteRequestByAdmin(request));
    }

    @Test
    void deleteRequest() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        assertTrue(rs.deleteRequest(request));
    }

    @Test
    void addAnswerToRequest() {
        RequestService rs;
        try {
            ServiceFactoryImpl sf = new ServiceFactoryImpl(new TransactionFactoryImpl());
            rs = sf.getService(RequestService.class);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        assertTrue(rs.addAnswerToRequest(request, document));
    }
}