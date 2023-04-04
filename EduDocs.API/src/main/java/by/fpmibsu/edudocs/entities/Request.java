package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;

public class Request extends Entity {
    RequestStatus status = RequestStatus.Sent;
    Timestamp created;
    Document document;

    public Request(@Nullable UUID id, @NotNull RequestStatus status, @Nullable Timestamp created,
                   @Nullable Document document) {
        super(id);
        this.status = status;
        this.created = created;
        this.document = document;
    }


}
