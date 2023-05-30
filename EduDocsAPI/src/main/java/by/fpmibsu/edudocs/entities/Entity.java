package by.fpmibsu.edudocs.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
@Getter
@Setter
@EqualsAndHashCode
public class Entity {
    UUID id;

    Entity(){}

    Entity(@Nullable UUID id){
        this.id = id;
    }
}
