package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserCreateAction extends AbstractAdministratorAction {

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
                logger.info("Cannot respond user with user creation, because user is not an admin");
            }
        }
        UserService service = factory.getService(UserService.class);
        Validator<User> validator = ValidatorFactory.createValidator(User.class);
        try {
            User user = validator.validate(request);
            service.addUser(user);
            response.setStatus(200);
        } catch (IncorrectFormDataException | IOException e) {
            logger.error("Error responding front with user creation");
            response.setStatus(500);
        }
    }
}
