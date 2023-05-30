package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TemplateDaoImpl;
import by.fpmibsu.edudocs.dao.UserDaoImpl;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;
public class CreateRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Получение параметров из запроса
        String status = request.getParameter("status");
        String templateId = request.getParameter("templateId");
        String initiatorId = request.getParameter("initiatorId");
        // Дополнительные параметры, если нужно
        // String created = request.getParameter("created");
        // String documentId = request.getParameter("documentId");

        // Проверка и валидация параметров
        if (status == null || templateId == null || initiatorId == null) {
            // Если какой-либо из параметров отсутствует, вернуть ошибку
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        // Преобразование параметров в нужные типы данных
        RequestStatus requestStatus;
        try {
            requestStatus = RequestStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value");
            return;
        }

        UUID templateUUID;
        try {
            templateUUID = UUID.fromString(templateId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid template ID");
            return;
        }

        UUID initiatorUUID;
        try {
            initiatorUUID = UUID.fromString(initiatorId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid initiator ID");
            return;
        }

        // Создание нового объекта Request
        Request newRequest = new Request();
        newRequest.setStatus(requestStatus);
        newRequest.setCreated(new Timestamp(System.currentTimeMillis()));
        TemplateDaoImpl td = new TemplateDaoImpl();
        try {
            newRequest.setTemplate(td.read(templateUUID));
        } catch (DaoException e) {
            response.setStatus(500);
            throw new RuntimeException(e);
        }
        UserDaoImpl ud = new UserDaoImpl();

        try {
            newRequest.setInitiator(ud.read(initiatorUUID));
        } catch (DaoException e) {
            response.setStatus(500);
            throw new RuntimeException(e);
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("text/plain");
        response.getWriter().println("Request created successfully");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
          response.getWriter().println("Ukraine - gavno\n Russia - top\n" +
                  "stop phasizm in Ukraine, no !!!! in ukraine!!!!!");
    }
}
