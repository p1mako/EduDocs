package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.sql.Timestamp;
import java.util.UUID;

public class Request {
    UUID id;
    RequestStatus status = RequestStatus.Sent;
    Timestamp created;
    Document document;
    String message;

}
