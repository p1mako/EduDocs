package by.fpmibsu.edudocs.entities;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
public class Document extends Entity {
    Template template;
    Timestamp created;
    Date validThrough;
    AdministrationMember author;
    User initiator;

    public Document() {
    }

    public Document(@Nullable UUID id, @NotNull Template template, @NotNull Timestamp created,
                    @Nullable Date validThrough, @Nullable AdministrationMember author, @NotNull User initiator) {
        super(id);
        this.template = template;
        this.created = created;
        this.validThrough = validThrough;
        this.author = author;
        this.initiator = initiator;
    }

}
