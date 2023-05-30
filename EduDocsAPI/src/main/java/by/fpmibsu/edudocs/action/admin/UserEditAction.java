package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.Role;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class UserEditAction extends AbstractAdministratorAction {

    private static final Logger logger = LogManager.getLogger(UserListAction.class);
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
                logger.info("Cannot respond user with user editon, because user is not an admin");
            }
        }
        request.setAttribute("roles", Role.values());
        UUID identity = UUID.fromString((String) request.getAttribute("identity"));
        UserService service = factory.getService(UserService.class);
        User user = service.findByIdentity(identity);
        if (user != null) {
            request.setAttribute("user", user);
        }
    }
}
