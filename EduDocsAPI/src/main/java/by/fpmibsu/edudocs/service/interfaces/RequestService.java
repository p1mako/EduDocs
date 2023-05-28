package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface RequestService extends Service {
    RequestStatus getRequestStatusFromDataSource(UUID identity);

    public boolean createRequest(Request request);

    public List<Request> getAllRequests();

    public boolean updateRequestStatus(Request request, RequestStatus status);

    public boolean deleteRequestByAdmin(Request request);

    public boolean deleteRequest(Request request);

    public boolean addAnswerToRequest(Request request, Document document);
}
