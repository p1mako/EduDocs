package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction extends AuthorizedUserAction {
    private static final Logger logger = LogManager.getLogger(LoginAction.class);
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        User user = getAuthorizedUser();
        logger.info(String.format("user \"%s\" is logged out", user.getLogin()));
        request.getSession(false).invalidate();
    }
}
