package by.fpmibsu.edudocs;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.UserDaoImpl;
import by.fpmibsu.edudocs.entities.User;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class App {

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
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
            Connection con = DriverManager.getConnection(url, prop);
            UserDaoImpl dao = new UserDaoImpl();
            dao.setConnection(con);
            ArrayList<User> list = (ArrayList<User>) dao.read();
            for (User user : list) {
                System.out.println(user.getLogin());
            }
        } catch (SQLException | DaoException e) {
            e.printStackTrace();
        }
    }
}
