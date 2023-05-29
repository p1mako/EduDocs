package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.ProfessorDaoImpl;
import by.fpmibsu.edudocs.dao.UserDaoImpl;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class AddProfessor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        ProfessorDaoImpl userDao = new ProfessorDaoImpl();

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

        if (request.getParameter("login") == null ||
                request.getParameter("password") == null ||
                request.getParameter("name") == null ||
                request.getParameter("surname") == null ||
                request.getParameter("degree") == null) {
            response.setStatus(422);
            return;
        }

//        try {
//            Professor professor = new Professor(
//                    request.getParameter("login"),
//                    request.getParameter("password"),
//                    request.getParameter("name"),
//                    request.getParameter("surname"),
//                    request.getParameter("lastName"),
//                    null,
//                    request.getParameter("degree")
//            );
//            if (userDao.create(professor)) {
//                response.setStatus(201);
//            } else {
//                response.setStatus(422);
//            }
//        } catch (DaoException e) {
//            response.setStatus(422);
//            return;
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user;
        UserDaoImpl userDao = new UserDaoImpl();
        try {
            user = userDao.findUserByLogin(request.getParameter("login"));
        } catch (DaoException e) {
            response.setStatus(401);
            return;
        }
        if (Objects.equals(user.getPassword(), request.getParameter("password"))) {
            HttpSession session = request.getSession(true);
        } else {
            response.setStatus(401);
        }
    }
}
