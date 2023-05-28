package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.Role;
import by.fpmibsu.edudocs.service.utils.ServiceFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class Action {
    private final Set<Role> allowRoles = new HashSet<>();
    private User authorizedUser;
    private String name;

    protected ServiceFactory factory;

    public Set<Role> getAllowRoles() {
        return allowRoles;
    }

    public User getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(User authorizedUser) {
        this.authorizedUser = authorizedUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFactory(ServiceFactory factory) {
        this.factory = factory;
    }

    abstract public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException;

}