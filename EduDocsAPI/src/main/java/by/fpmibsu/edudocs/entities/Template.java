package by.fpmibsu.edudocs.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Template extends Entity {
    String name;
    String routeToDocument;

    Template() {
    }

    public Template(@Nullable UUID id, @NotNull String name, @Nullable String routeToDocument) {
        super(id);
        this.name = name;
        this.routeToDocument = routeToDocument;
    }
}
