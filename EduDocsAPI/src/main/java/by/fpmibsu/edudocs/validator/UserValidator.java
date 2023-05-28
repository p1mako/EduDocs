package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class UserValidator implements Validator<User> {
    @Override
    public User validate(HttpServletRequest request) throws IOException {
        return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), User.class);
    }
}
