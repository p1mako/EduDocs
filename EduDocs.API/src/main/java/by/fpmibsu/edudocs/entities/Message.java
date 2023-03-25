package by.fpmibsu.edudocs.entities;

import java.sql.Timestamp;

public class Message extends Entity{
    String text;
    Timestamp time;
    Request request;
}
