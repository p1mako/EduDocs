package by.fpmibsu.edudocs.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Professor extends User {
    private List<Request> requests;
    public Professor(){}

    public Professor(@NotNull String login, @NotNull String password, @NotNull String name, @NotNull String surname,
                     @Nullable String lastName, @Nullable UUID id, @NotNull String degree, List<Request> requests) {
        super(login, password, name, surname, lastName, id);
        this.degree = degree;
        this.requests = requests;
    }
    String degree;
}
