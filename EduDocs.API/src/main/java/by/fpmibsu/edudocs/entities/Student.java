package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.StudentStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;

public class Student extends User {
    Timestamp entryDate;
    int group;
    StudentStatus status;
    int uniqueNumber;
    Specialization specialization;


    public Student(@Nullable UUID id, @NotNull String login, @NotNull String password, @NotNull String name,
                   @NotNull String surname, @Nullable String lastName, @NotNull Timestamp entryDate,
                   @Nullable int group, @NotNull int uniqueNumber, @NotNull StudentStatus status,
                   @NotNull Specialization specialization) {
        super(login, password, name, surname, lastName, id);
        this.entryDate = entryDate;
        this.group = group;
        this.status = status;
        this.uniqueNumber = uniqueNumber;
        this.specialization = specialization;
    }

}
