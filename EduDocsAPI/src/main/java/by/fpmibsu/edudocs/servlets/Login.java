package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.UserDao;
import by.fpmibsu.edudocs.entities.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        UserDao userDao = new UserDao();

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
            userDao.setConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (request.getParameter("login") == null || request.getParameter("password") == null) {
            response.setStatus(401);
            return;
        }
        try {
            user = userDao.findEntityByLogin(request.getParameter("login"));
        } catch (DaoException e) {
            response.setStatus(401);
            return;
        }
        if (user.getPassword().equals(request.getParameter("password"))) {
            HttpSession session = request.getSession(true);
            java.io.PrintWriter writer = response.getWriter();
            writer.println("Success!");
        } else {
            response.setStatus(401);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}