package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.RequestStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.UUID;

public class Request extends Entity {
    RequestStatus status;
    Timestamp created;
    Document document;

    public Request(){}

    public Request(@Nullable UUID id, @NotNull RequestStatus status, @Nullable Timestamp created,
                   @Nullable Document document) {
        super(id);
        this.status = status;
        this.created = created;
        this.document = document;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
