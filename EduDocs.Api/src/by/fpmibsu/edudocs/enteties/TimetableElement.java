package by.fpmibsu.edudocs.enteties;

import by.fpmibsu.edudocs.enteties.utils.LessonType;
import by.fpmibsu.edudocs.enteties.utils.WeekDay;

import java.util.UUID;

public class TimetableElement {
    UUID id;
    String subject;
    WeekDay dayOfWeek;
    int lessonNum;
    String auditorium;
    String location;
    Professor professor;
    LessonType lessonType;

}
