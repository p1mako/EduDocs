package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.util.UUID;

public interface RequestService extends Service {
    RequestStatus getRequestStatusFromDataSource(UUID identity);

}
