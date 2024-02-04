package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface RequestService extends Service {
    RequestStatus getRequestStatusFromDataSource(UUID identity) throws DaoException;

    public boolean createRequest(Request request) throws DaoException;

    public List<Request> getAllRequests() throws DaoException;

    public boolean updateRequestStatus(Request request, RequestStatus status) throws DaoException;

    public boolean deleteRequestByAdmin(Request request) throws DaoException;

    public boolean deleteRequest(Request request) throws DaoException;

    public boolean addAnswerToRequest(Request request, Document document) throws DaoException;

    public boolean updateRequest(Request request) throws DaoException;
}
