package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserListAction extends AbstractAdministratorAction {
	@Override
	public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
		UserService service = factory.getService(UserService.class);
		List<User> users = service.findAll();
		request.setAttribute("users", users);
	}
}
