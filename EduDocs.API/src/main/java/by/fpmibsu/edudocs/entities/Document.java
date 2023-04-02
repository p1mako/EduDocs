package by.fpmibsu.edudocs.entities;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class Document extends Entity{
    Template template;
    Timestamp created;
    Date validThrough;
    AdministrationMember author;
    User initiator;
}
