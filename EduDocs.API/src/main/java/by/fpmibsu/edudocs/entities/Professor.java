package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Professor extends User{
    public Professor(@NotNull String login, @NotNull String password, @NotNull String name, @NotNull String surname,
                     @Nullable String lastName, @Nullable UUID id, @NotNull String degree) {
        super(login, password, name, surname, lastName, id);
        this.degree = degree;
    }

    String degree;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
