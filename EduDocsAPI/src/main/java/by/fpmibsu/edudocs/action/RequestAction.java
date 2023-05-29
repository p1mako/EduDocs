package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.Entity;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.service.interfaces.RequestService;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.validator.RequestValidator;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestAction extends Action {
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        RequestValidator validator = new RequestValidator();
        Request myRequest;
        try {
            myRequest = validator.validate(request);
        } catch (IncorrectFormDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RequestService service = factory.getService(RequestService.class);

        if (service.createRequest(myRequest)) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }
    }
}
