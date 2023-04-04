package by.fpmibsu.edudocs.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AdministrationDocuments extends Entity{
    AdministrationMember administration;
    Template template;

    public AdministrationDocuments(@Nullable UUID id, @NotNull AdministrationMember administration, @NotNull Template template) {
        super(id);
        this.administration = administration;
        this.template = template;
    }
}
