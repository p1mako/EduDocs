package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.ProfessorDao;
import by.fpmibsu.edudocs.entities.Professor;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@WebServlet(name = "UpdateProfessor", value = "/update-professor")
public class UpdateProfessor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProfessorDao professorDao = new ProfessorDao();

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
            professorDao.setConnection(con);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        if (request.getParameter("id") == null ||
                request.getParameter("login") == null ||
                request.getParameter("password") == null ||
                request.getParameter("name") == null ||
                request.getParameter("surname") == null ||
                request.getParameter("degree") == null){
            response.setStatus(422);
            return;
        }

        try {
            Professor professor = new Professor(
                    request.getParameter("login"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("surname"),
                    request.getParameter("lastName"),
                    UUID.fromString(request.getParameter("id")),
                   request.getParameter("degree")
            );
            if(professorDao.update(professor)){
                response.setStatus(200);
            }else{
                response.setStatus(422);
            }
        } catch (DaoException e) {
            response.setStatus(422);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}