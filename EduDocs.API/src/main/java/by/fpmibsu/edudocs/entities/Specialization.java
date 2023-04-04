package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Specialization extends Entity {
    String name;
    int registerNumber;


    public Specialization(@Nullable UUID id, @NotNull String name, int registerNumber) {
        super(id);
        this.name = name;
        this.registerNumber = registerNumber;
    }

}
