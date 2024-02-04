package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDaoImpl;
import by.fpmibsu.edudocs.dao.interfaces.RequestDao;
import by.fpmibsu.edudocs.entities.Document;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.service.utils.AbstractService;

import java.util.List;
import java.util.UUID;

public class RequestServiceImpl extends AbstractService implements RequestService {
    @Override
    public RequestStatus getRequestStatusFromDataSource(UUID identity) throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        var request = rd.read(identity);
        return request.getStatus();
    }

    @Override
    public boolean createRequest(Request request) throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        rd.create(request);
        return true;
    }

    @Override
    public List<Request> getAllRequests() throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        var requests = rd.read();
        return requests;
    }

    @Override
    public boolean updateRequestStatus(Request request, RequestStatus status) throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        request.setStatus(status);
        rd.update(request);
        return true;
    }

    @Override
    public boolean deleteRequestByAdmin(Request request) throws DaoException {
        return updateRequestStatus(request, RequestStatus.Removed);
    }

    @Override
    public boolean deleteRequest(Request request) throws DaoException {
        return updateRequestStatus(request, RequestStatus.Declined);
    }

    @Override
    public boolean addAnswerToRequest(Request request, Document document) throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        try {
            request.setDocument(document);
            rd.update(request);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public boolean updateRequest(Request request) throws DaoException {
        RequestDao rd = transaction.createDao(RequestDao.class);
        try {
            rd.update(request);
            return true;
        } catch (DaoException e) {
            return false;
        }
    }
}
