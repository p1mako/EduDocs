package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.StudentStatus;

import java.sql.Timestamp;

public class Student extends User{
    Timestamp entryDate;
    int group;
    Specialization spec;
    StudentStatus status;
    int uniqueNumber;
}
