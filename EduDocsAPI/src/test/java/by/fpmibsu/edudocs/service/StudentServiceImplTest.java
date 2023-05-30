package java.by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import by.fpmibsu.edudocs.entities.Specialization;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;
import by.fpmibsu.edudocs.service.interfaces.StudentService;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.UUID;

import static org.testng.Assert.*;

public class StudentServiceImplTest {

    private static StudentService studentService;
        @DataProvider(name="data-provider")
        public Object[][] dataProviderMethod() {

            Student studentEx = new Student(UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9"),
                    "fpm.Hartov",
                    "o98,pluj68h7egv",
                    "Stanislave",
                    "Hartov",
                    "Vitalievich",
                    Timestamp.valueOf("2021-08-01 00:00:00.0000000"),
                    1,
                    2123202,
                    StudentStatus.Learning,
                    new Specialization(UUID.fromString("E79FC51F-54A9-4AF7-B840-A7D841EDBAB4"), "Informatics", "1-31 03 04"),
                    null
                    );

            return new Object[][] {
                    {UUID.fromString("6386925c-fed4-11ed-be56-0242ac120002")},
                    {null},
                    {UUID.fromString("7B9FE49C-BDD8-4563-B28B-1E8A657E23A9")},
            };
        }

    @BeforeMethod
    public void setUp() throws DaoException {
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
        studentService = new ServiceFactoryImpl(new TransactionFactoryImpl()).getService(StudentService.class);
    }

    @Test(dataProvider = "data-provider")
    public void testFindByIdentity(UUID identity) throws DaoException {
            studentService.findByIdentity(identity);
    }
}