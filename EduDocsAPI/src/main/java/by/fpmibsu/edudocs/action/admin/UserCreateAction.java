package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserCreateAction extends AbstractAdministratorAction {
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        UserService service = factory.getService(UserService.class);
        Validator<User> validator = ValidatorFactory.createValidator(User.class);
        try {
            User user = validator.validate(request);
            service.addUser(user);
        } catch (IncorrectFormDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
