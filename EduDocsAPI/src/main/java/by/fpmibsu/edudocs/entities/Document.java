package by.fpmibsu.edudocs.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class Document extends Entity {
    Template template;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    Timestamp created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
