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
                   int group, int uniqueNumber, @NotNull StudentStatus status,
                   @NotNull Specialization specialization) {
        super(login, password, name, surname, lastName, id);
        this.entryDate = entryDate;
        this.group = group;
        this.status = status;
        this.uniqueNumber = uniqueNumber;
        this.specialization = specialization;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(int uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
