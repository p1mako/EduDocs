package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.AdministrationRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;

public class AdministrationMember extends User {

    public AdministrationMember(@NotNull AdministrationRole role, @NotNull Timestamp from, @Nullable Timestamp until,
                                @NotNull String login, @NotNull String password, @NotNull String name,
                                @NotNull String surname, @Nullable String lastName, @Nullable UUID id) {
        super(login, password, name, surname, lastName, id);
        this.role = role;
        this.from = from;
        this.until = until;
    }

    AdministrationRole role;
    Timestamp from;
    Timestamp until;
}
