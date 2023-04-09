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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteToDocument() {
        return routeToDocument;
    }

    public void setRouteToDocument(String routeToDocument) {
        this.routeToDocument = routeToDocument;
    }
}
