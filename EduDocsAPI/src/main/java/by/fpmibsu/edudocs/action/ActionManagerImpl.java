package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.service.utils.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionManagerImpl implements ActionManager {
    private final ServiceFactory factory;

    public ActionManagerImpl(ServiceFactory factory) {
        this.factory = factory;
    }

    @Override
    public void execute(Action action, HttpServletRequest request, HttpServletResponse response) throws DaoException {
        action.setFactory(factory);
        action.exec(request, response);
    }

    @Override
    public void close() {
        factory.close();
    }
}
