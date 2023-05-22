package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDao;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;
@WebServlet(name = "CheckRequestStatus", value = "/CheckRequestStatus")

public class CheckRequestStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the request ID from the request parameters
        String requestId = request.getParameter("requestId");

        // Validate the request ID parameter
        if (requestId == null || requestId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required");
            return;
        }

        // Parse the request ID as UUID
        UUID requestIdUUID;
        try {
            requestIdUUID = UUID.fromString(requestId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID");
            return;
        }

        // Query the database or data source to get the request status
        RequestStatus requestStatus = getRequestStatusFromDataSource(requestIdUUID);

        if (requestStatus == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            return;
        }

        // Send the request status as the response
        response.setContentType("text/plain");
        response.getWriter().println("Request status: " + requestStatus);
    }

    private RequestStatus getRequestStatusFromDataSource(UUID requestId) {
        RequestDao rd = new RequestDao();
        try {
            var request = rd.findEntityById(requestId);
            return request.getStatus();
        } catch (DaoException e) {
            return null;
        }
    }
}
