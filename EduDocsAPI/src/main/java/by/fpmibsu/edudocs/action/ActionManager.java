package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionManager {
    Action.Forward execute(Action action, HttpServletRequest request, HttpServletResponse response) throws DaoException;

    void close();
}
