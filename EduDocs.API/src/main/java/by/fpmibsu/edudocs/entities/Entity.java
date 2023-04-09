package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Entity {
    UUID id;

    Entity(){}

    Entity(@Nullable UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
