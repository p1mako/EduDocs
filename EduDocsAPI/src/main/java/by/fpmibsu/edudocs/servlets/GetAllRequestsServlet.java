package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDao;
import by.fpmibsu.edudocs.entities.Request;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "GetAllRequests", value = "/get-all-requests")
public class GetAllRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String id = request.getParameter("requestId");
        try (PrintWriter writer = response.getWriter()) {
            if (request.getSession(false) == null) {
                response.setStatus(403);
                return;
            }
            var rd = new RequestDao();
            var list = rd.findAll();
            List<Request> result = new ArrayList<>();
            for (var req : list) {
                if (req.getId().equals(UUID.fromString(id))) {
                    result.add(req);
                }
            }
            writer.println(result);

            writer.println("<h2>" + request.getParameter("user") + "</h2>");
            writer.println("<h2>" + request.getParameter("password") + "</h2>");
        } catch (DaoException e) {
            // da
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}