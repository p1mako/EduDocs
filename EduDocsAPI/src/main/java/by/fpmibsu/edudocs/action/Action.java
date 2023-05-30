package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.Role;
import by.fpmibsu.edudocs.service.utils.ServiceFactory;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
abstract public class Action {
    private final Set<Role> allowRoles = new HashSet<>();
    private User authorizedUser;
    private String name;

    protected ServiceFactory factory;

    public Set<Role> getAllowRoles() {
        return allowRoles;
    }

    abstract public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException;

}