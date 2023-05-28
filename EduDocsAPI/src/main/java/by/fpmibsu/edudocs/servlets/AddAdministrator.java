package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.AdministrationMemberDaoImpl;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.utils.AdministrationRole;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

@WebServlet(name = "AddAdministrator", value = "/add-admin")
public class AddAdministrator extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AdministrationMemberDaoImpl adminMemberDao = new AdministrationMemberDaoImpl();

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
            adminMemberDao.setConnection(con);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        if (request.getParameter("login") == null ||
                request.getParameter("password") == null ||
                request.getParameter("name") == null ||
                request.getParameter("surname") == null ||
                request.getParameter("role") == null ||
                request.getParameter("from") == null){
            response.setStatus(422);
            return;
        }

        try {
            AdministrationMember adminMember = new AdministrationMember(
                    UUID.fromString(request.getParameter("id")),
                    AdministrationRole.valueOf(request.getParameter("role")),
                    Timestamp.valueOf(request.getParameter("from")),
                    request.getParameter("until") != null ? Timestamp.valueOf(request.getParameter("until")) : null,
                    request.getParameter("login"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("surname"),
                    request.getParameter("lastName"),
                    new ArrayList<Template>()
            );
            if(adminMemberDao.create(adminMember) == null){
                response.setStatus(201);
            }else{
                response.setStatus(422);
            }
        } catch (DaoException e) {
            response.setStatus(422);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
