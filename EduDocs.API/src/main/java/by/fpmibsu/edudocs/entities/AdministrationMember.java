package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.AdministrationRole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AdministrationMember extends User {

    AdministrationRole role;
    Timestamp from;
    Timestamp until;
    List<Template> availableTemplates;

    public AdministrationMember(@Nullable UUID id, @NotNull AdministrationRole role, @NotNull Timestamp from,
                                @Nullable Timestamp until, @NotNull String login, @NotNull String password,
                                @NotNull String name, @NotNull String surname, @Nullable String lastName,
                                @Nullable List<Template> availableTemplates) {
        super(login, password, name, surname, lastName, id);
        this.role = role;
        this.from = from;
        this.until = until;
        this.availableTemplates = availableTemplates;
    }
}
