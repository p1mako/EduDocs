package by.fpmibsu.edudocs.action.admin;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.Role;
import by.fpmibsu.edudocs.service.interfaces.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class UserEditAction extends AbstractAdministratorAction {
    @Override
    public Forward exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        request.setAttribute("roles", Role.values());
        UUID identity = UUID.fromString((String) request.getAttribute("identity"));
        UserService service = factory.getService(UserService.class);
        User user = service.findByIdentity(identity);
        if (user != null) {
            request.setAttribute("user", user);
        }
        return null;
    }
}
