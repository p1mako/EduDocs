package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.LessonType;
import by.fpmibsu.edudocs.entities.utils.WeekDay;

import java.util.UUID;

public class TimetableElement extends Entity {
    Discipline subject;
    WeekDay dayOfWeek;
    int lessonNum;
    Location location;
    Professor professor;
    LessonType lessonType;

}
