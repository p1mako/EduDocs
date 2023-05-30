package by.fpmibsu.edudocs.entities.utils;

public enum AdministrationRole {
    DEAN("dean"),
    EDUCATIONAL_DEPUTY("educational_deputy"),
    ACADEMIC_DEPUTY("academic_deputy");

    private final String role;

    AdministrationRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Integer getIdentity() {
        return ordinal();
    }

    public static AdministrationRole getByIdentity(Integer identity) {
        return AdministrationRole.values()[identity];
    }
}