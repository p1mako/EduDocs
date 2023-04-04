package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;

public class Message extends Entity {
    String text;
    Timestamp time;
    Request request;

    public Message(@Nullable UUID id, @NotNull String text,@Nullable Timestamp time, @NotNull Request request) {
        super(id);
        this.text = text;
        this.time = time;
        this.request = request;
    }


}
