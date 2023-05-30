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

public class UserSaveAction extends AbstractAdministratorAction {
	private static final Logger logger = LogManager.getLogger(UserSaveAction.class);

	@Override
	public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
		//Forward forward = new Forward("/user/edit.html");
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
			Validator<User> validator = ValidatorFactory.createValidator(User.class);
			User user = validator.validate(request);
			UserService service = factory.getService(UserService.class);
			service.save(user);
			response.setStatus(200);
			logger.info(String.format("User \"%s\" saved user with identity %s", getAuthorizedUser().getLogin(), user.getId()));
		} catch(IncorrectFormDataException e) {
			response.setStatus(422);
			logger.warn(String.format("Incorrect data was found when user \"%s\" tried to save user", getAuthorizedUser().getLogin()), e);
		} catch (IOException e) {
			response.setStatus(500);
		}
	}
}
