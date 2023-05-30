package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction extends AuthorizedUserAction {
    private static final Logger logger = LogManager.getLogger(LoginAction.class);
    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        var userStr = (String) request.getSession().getAttribute("user");
        if (userStr == null){
            return;
        }
        User user = null;
        try {
            user = new ObjectMapper().readValue(userStr, User.class);
        } catch (JsonProcessingException e) {
            logger.error("Cannot parse user attribute");
        }
        logger.info(String.format("user \"%s\" is logged out", user.getLogin()));
        request.getSession(false).invalidate();
    }
}
