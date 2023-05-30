package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class UserDeleteAction extends AbstractAdministratorAction {
    private static final Logger logger = LogManager.getLogger(UserDeleteAction.class);

    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        if (request.getSession(false) == null) {
            logger.error("Session is null");
            response.setStatus(401);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.readValue(request.getSession().getAttribute("user").toString(), AdministrationMember.class);
            } catch (JsonProcessingException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.info("Cannot respond user with user list, because user is not an admin");
            }
        }

        try {
            UserService service = factory.getService(UserService.class);
            UUID identity = UUID.fromString(request.getParameter("identity"));
            service.delete(identity);
            logger.info(String.format("User \"%s\" deleted user with identity %s", getAuthorizedUser().getLogin(), identity));
            response.setStatus(200);
        } catch (NumberFormatException e) {
            response.setStatus(500);
            logger.warn(String.format("Incorrect data was found when user \"%s\" tried to delete user", getAuthorizedUser().getLogin()), e);
        }

    }
}
