package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.RequestDaoImpl;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import by.fpmibsu.edudocs.service.interfaces.RequestService;

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
}
