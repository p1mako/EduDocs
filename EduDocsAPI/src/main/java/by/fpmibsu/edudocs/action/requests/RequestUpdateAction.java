package by.fpmibsu.edudocs.action.requests;

import by.fpmibsu.edudocs.action.Action;
import by.fpmibsu.edudocs.action.admin.UserListAction;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestUpdateAction extends Action {

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
                logger.info("Cannot respond user with user list, because user is not an admin");
            }
        }

        Validator<Request> validator = ValidatorFactory.createValidator(Request.class);
        Request myRequest;
        try {
            myRequest = validator.validate(request);
        } catch (IncorrectFormDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RequestService service = factory.getService(RequestService.class);

        if (service.updateRequest(myRequest)) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }
    }
}
