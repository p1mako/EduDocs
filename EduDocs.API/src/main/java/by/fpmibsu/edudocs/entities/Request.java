package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.RequestStatus;

import java.sql.Timestamp;
import java.util.UUID;

public class Request extends Entity{
    RequestStatus status = RequestStatus.Sent;
    Timestamp created;
    Document document;
}
