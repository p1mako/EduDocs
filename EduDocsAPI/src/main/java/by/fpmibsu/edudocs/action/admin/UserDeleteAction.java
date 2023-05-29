package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class UserDeleteAction extends AbstractAdministratorAction {
    private static final Logger logger = LogManager.getLogger(UserDeleteAction.class);
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {

    }
}
