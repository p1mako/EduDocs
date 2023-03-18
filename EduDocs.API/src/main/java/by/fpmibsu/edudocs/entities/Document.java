package by.fpmibsu.edudocs.entities;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class Document {
    UUID id;
    Template template;
    Timestamp created;
    Date validThrough;
    AdministrationMember author;
    User initiator;
}
