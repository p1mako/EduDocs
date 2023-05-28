package by.fpmibsu.edudocs.validator;

import main.java.by.avelana.library.domain.Role;
import main.java.by.avelana.library.domain.User;
import main.java.by.avelana.library.exception.IncorrectFormDataException;

import javax.servlet.http.HttpServletRequest;

public class UserValidator implements Validator<User> {
	@Override
	public User validate(HttpServletRequest request) throws IncorrectFormDataException {
		User user = new User();
		String parameter = request.getParameter("identity");
		if(parameter != null) {
			try {
				user.setIdentity(Integer.parseInt(parameter));
			} catch(NumberFormatException e) {
				throw new IncorrectFormDataException("identity", parameter);
			}
		}
		parameter = request.getParameter("login");
		if(parameter != null && !parameter.isEmpty()) {
			user.setLogin(parameter);
		} else {
			throw new IncorrectFormDataException("login", parameter);
		}
		parameter = request.getParameter("role");
		try {
			user.setRole(Role.getByIdentity(Integer.parseInt(parameter)));
		} catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
			throw new IncorrectFormDataException("role", parameter);
		}
		return user;
	}
}
