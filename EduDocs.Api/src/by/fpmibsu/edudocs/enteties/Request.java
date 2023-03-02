package by.fpmibsu.edudocs.enteties;

import by.fpmibsu.edudocs.enteties.utils.RequestStatus;

import java.sql.Timestamp;
import java.util.UUID;

public class Request {
    UUID id;
    RequestStatus status = RequestStatus.Sent;
    Timestamp created;
    Template template;
    Document document;
    String message;

}
