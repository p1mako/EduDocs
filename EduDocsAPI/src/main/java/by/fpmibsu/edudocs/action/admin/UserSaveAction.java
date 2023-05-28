package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.validator.Validator;
import by.fpmibsu.edudocs.validator.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSaveAction extends AbstractAdministratorAction {
	private static final Logger logger = LogManager.getLogger(UserSaveAction.class);

	@Override
	public Forward exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
		Forward forward = new Forward("/user/edit.html");
		try {
			Validator<User> validator = ValidatorFactory.createValidator(User.class);
			User user = validator.validate(request);
			UserService service = factory.getService(UserService.class);
			service.save(user);
			forward.getAttributes().put("identity", user.getId());
			forward.getAttributes().put("message", "Данные пользователя успешно сохранены");
			logger.info(String.format("User \"%s\" saved user with identity %s", getAuthorizedUser().getLogin(), user.getId()));
		} catch(IncorrectFormDataException e) {
			forward.getAttributes().put("message", "Были обнаружены некорректные данные");
			logger.warn(String.format("Incorrect data was found when user \"%s\" tried to save user", getAuthorizedUser().getLogin()), e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return forward;
	}
}
