package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDaoImpl;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import by.fpmibsu.edudocs.service.interfaces.RequestService;

import java.util.List;
import java.util.UUID;

public class RequestServiceImpl implements RequestService {
    @Override
    public RequestStatus getRequestStatusFromDataSource(UUID identity) {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            var request = rd.read(identity);
            return request.getStatus();
        } catch (DaoException e) {
            return null;
        }
    }

    @Override
    public boolean createRequest(Request request) {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            rd.create(request);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public List<Request> getAllRequests() {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            List<Request> list= rd.read();;
            return list;
        } catch (DaoException e) {
            return null;
        }
    }

    @Override
    public boolean updateRequestStatus(Request request, RequestStatus status) {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            request.setStatus(status);
            rd.update(request);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public boolean deleteRequestByAdmin(Request request) {
        return updateRequestStatus(request, RequestStatus.Removed);
    }

    @Override
    public boolean deleteRequest(Request request) {
        return updateRequestStatus(request, RequestStatus.Declined);
    }

    @Override
    public boolean addAnswerToRequest(Request request, Document document) {
        RequestDaoImpl rd = new RequestDaoImpl();
        try {
            request.setDocument(document);
            rd.update(request);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }
}
