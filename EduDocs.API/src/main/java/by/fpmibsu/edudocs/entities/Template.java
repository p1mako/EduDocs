package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Template extends Entity {
    String name;
    String routeToDocument;

    public Template(@Nullable UUID id, @NotNull String name, @Nullable String routeToDocument) {
        super(id);
        this.name = name;
        this.routeToDocument = routeToDocument;
    }
}
