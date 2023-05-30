package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.action.LoginAction;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class RequestValidator implements Validator<Request> {

    private static final Logger logger = LogManager.getLogger(LoginAction.class);
    @Override
    public Request validate(HttpServletRequest request) throws IncorrectFormDataException, IOException {
        return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), Request.class);
    }
}
