package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.StudentStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Student extends User {
    Timestamp entryDate;
    int group;
    StudentStatus status;
    int uniqueNumber;
    Specialization specialization;

    List<Request> requests;

    public Student() {
    }

    public Student(@Nullable UUID id, @NotNull String login, @NotNull String password, @NotNull String name,
                   @NotNull String surname, @Nullable String lastName, @NotNull Timestamp entryDate,
                   int group, int uniqueNumber, @NotNull StudentStatus status,
                   @NotNull Specialization specialization, List<Request> requests) {
        super(login, password, name, surname, lastName, id);
        this.entryDate = entryDate;
        this.group = group;
        this.status = status;
        this.uniqueNumber = uniqueNumber;
        this.specialization = specialization;
        this.requests = requests;
    }
}
