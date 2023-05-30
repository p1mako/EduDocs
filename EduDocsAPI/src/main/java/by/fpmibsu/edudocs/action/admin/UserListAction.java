package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserListAction extends AbstractAdministratorAction {

	private static final Logger logger = LogManager.getLogger(UserListAction.class);
	@Override
	public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {

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

		UserService service = factory.getService(UserService.class);
		List<User> users = service.findAll();
		ObjectMapper mapper = new ObjectMapper();
		try {
			response.getOutputStream().println(mapper.writeValueAsString(users));
			response.setStatus(200);
		} catch (IOException e) {
			logger.error("Error responding front with user list");
			response.setStatus(500);
		}
	}
}
