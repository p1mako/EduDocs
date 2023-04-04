package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class User extends Entity{

    public User(@NotNull String login, @NotNull String password, @NotNull String name, @NotNull String surname,
                @Nullable String lastName, @Nullable UUID id){
        super(id);
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
    }
    String login;
    String password;
    String name;
    String surname;
    String lastName;

}
