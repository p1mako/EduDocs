package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDaoImpl;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

public class ChangeRequestStateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the request ID and new status from the request parameters
        String requestId = request.getParameter("requestId");
        String newStatus = request.getParameter("newStatus");

        // Validate the request ID and new status parameters
        if (requestId == null || requestId.isEmpty() || newStatus == null || newStatus.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID and new status are required");
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

        // Validate the new status
        RequestStatus requestStatus;
        try {
            requestStatus = RequestStatus.valueOf(newStatus);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid new status");
            return;
        }

        // Update the request status in the database or data source
        boolean updateSuccess = updateRequestStatusInDataSource(requestIdUUID, requestStatus);

        if (!updateSuccess) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            return;
        }

        // Send a success response
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println("Request status updated successfully");
    }

    private boolean updateRequestStatusInDataSource(UUID requestId, RequestStatus newStatus) {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            var request = rd.read(requestId);
            request.setStatus(newStatus);
            rd.update(request);
        } catch (DaoException e) {
            return false;
        }
        // Update the request status in the database or data source based on the request ID
        // Implement your logic here to update the request status

        // For demonstration purposes, assume the update was successful
        return true;
    }
}
