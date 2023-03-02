package enteties;

import enteties.utils.LessonType;
import enteties.utils.WeekDay;

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
