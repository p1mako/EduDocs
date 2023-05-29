package by.fpmibsu.edudocs.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class User extends Entity {

    String login;
    String password;
    String name;
    String surname;
    String lastName;

    public User() {
    }

    public User(@NotNull String login, @NotNull String password, @NotNull String name, @NotNull String surname,
                @Nullable String lastName, @Nullable UUID id) {
        super(id);
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
    }
}
