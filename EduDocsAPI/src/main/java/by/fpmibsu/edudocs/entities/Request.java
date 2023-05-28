package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;
@Getter
@Setter
public class Request extends Entity {
    RequestStatus status;
    Timestamp created;
    Document document;
    Template template;


    User initiator;

    public Request() {
    }

    public Request(@Nullable UUID id, @NotNull RequestStatus status, @NotNull Template template, @NotNull User initiator,
                   @Nullable Timestamp created, @Nullable Document document) {
        super(id);
        this.template = template;
        this.initiator = initiator;
        this.status = status;
        this.created = created;
        this.document = document;
    }
}
