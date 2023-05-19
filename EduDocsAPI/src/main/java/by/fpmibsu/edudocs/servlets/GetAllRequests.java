package by.fpmibsu.edudocs.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetAllRequests", value = "/get-all-requests")
public class GetAllRequests extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        if (request.getSession(false) == null){
            response.setStatus(403);
            return;
        }

        try {
            writer.println("<h2>" + request.getParameter("user") + "</h2>");
            writer.println("<h2>" + request.getParameter("password") + "</h2>");
        } finally {
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}