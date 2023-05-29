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
	public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {

	}
}
