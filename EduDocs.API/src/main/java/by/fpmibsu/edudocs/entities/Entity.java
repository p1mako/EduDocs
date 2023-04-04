package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Entity {
    UUID id;

    Entity(@Nullable UUID id){
        this.id = id;
    }
}
