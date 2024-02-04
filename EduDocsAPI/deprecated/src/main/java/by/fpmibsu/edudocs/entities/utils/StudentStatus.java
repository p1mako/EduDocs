package by.fpmibsu.edudocs.entities.utils;

public enum StudentStatus {
    Learning("Learning"),
    AcademicVacation("AcademicVacation");

    private final String status;

    StudentStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public Integer getIdentity() {
        return ordinal();
    }

    public static StudentStatus getByIdentity(Integer identity) {
        return StudentStatus.values()[identity];
    }
}
