package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.action.Action;
import by.fpmibsu.edudocs.entities.utils.Role;

public abstract class AbstractAdministratorAction extends Action {
    public AbstractAdministratorAction() {
        getAllowRoles().add(Role.Admin);
    }
}
