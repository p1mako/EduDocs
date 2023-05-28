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
    public Forward exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        Forward forward = new Forward("/user/list.html");
        try {
            UserService service = factory.getService(UserService.class);
            UUID identity = UUID.fromString(request.getParameter("identity"));
            service.delete(identity);
            forward.getAttributes().put("message", "Пользователь успешно удалён");
            logger.info(String.format("User \"%s\" deleted user with identity %s", getAuthorizedUser().getLogin(), identity));
        } catch(NumberFormatException e) {
            logger.warn(String.format("Incorrect data was found when user \"%s\" tried to delete user", getAuthorizedUser().getLogin()), e);
        }
        return forward;
    }
}
