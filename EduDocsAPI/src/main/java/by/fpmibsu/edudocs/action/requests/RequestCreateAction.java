package by.fpmibsu.edudocs.action.requests;

import by.fpmibsu.edudocs.action.Action;
import by.fpmibsu.edudocs.action.admin.UserListAction;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestCreateAction extends Action {

    private static final Logger logger = LogManager.getLogger(UserListAction.class);
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {

        if (request.getSession(false) == null) {
            logger.error("Session is null");
            response.setStatus(401);
        }

        Validator<Request> validator = ValidatorFactory.createValidator(Request.class);
        Request myRequest;
        try {
            myRequest = validator.validate(request);
            RequestService service = factory.getService(RequestService.class);

            if (service.createRequest(myRequest)) {
                logger.info(String.format("request with id %s has been created", myRequest.getId().toString()));
                response.setStatus(200);
            } else {
                logger.info(String.format("request with id %s hasn`t been created", myRequest.getId().toString()));
                response.setStatus(500);
            }
        } catch (IncorrectFormDataException | IOException  e) {
            logger.info("request is wrong");
            response.setStatus(422);
        }
    }
}
