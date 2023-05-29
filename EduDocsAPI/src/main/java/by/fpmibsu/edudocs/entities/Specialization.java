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
public class Specialization extends Entity {
    String name;
    String registerNumber;

    public Specialization() {
    }

    public Specialization(@Nullable UUID id, @NotNull String name, String registerNumber) {
        super(id);
        this.name = name;
        this.registerNumber = registerNumber;
    }

}
