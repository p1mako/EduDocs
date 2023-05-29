package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.entities.utils.Role;

import java.util.Arrays;

public abstract class AuthorizedUserAction extends Action {
    public AuthorizedUserAction() {
        getAllowRoles().addAll(Arrays.asList(Role.values()));
    }
}
