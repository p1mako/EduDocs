package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.interfaces.AdministrationMemberService;
import by.fpmibsu.edudocs.service.interfaces.ProfessorService;
import by.fpmibsu.edudocs.service.interfaces.StudentService;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.validator.UserValidator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginAction extends Action {

    private static final Logger logger = LogManager.getLogger(LoginAction.class);


    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        String login = null;
        String password = null;
        try {
            String toParse = request.getReader().readLine();
            if (toParse != null) {
                var jp = (new JsonFactory()).createParser(request.getReader().readLine());
                login = jp.getValueAsString("login");
                password = jp.getValueAsString("password");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (login != null && password != null) {
            UserService service = factory.getService(UserService.class);
            User user = service.findByLogin(login);
            if (user != null) {
                HttpSession session = request.getSession();
                Student student;
                Professor professor;
                AdministrationMember administrationMember;
                StudentService ss = factory.getService(StudentService.class);
                student = ss.findByIdentity(user.getId());
                if (student == null) {
                    ProfessorService ps = factory.getService(ProfessorService.class);
                    professor = ps.findByIdentity(user.getId());
                    if (professor == null) {
                        AdministrationMemberService as = factory.getService(AdministrationMemberService.class);
                        administrationMember = as.findByIdentity(user.getId());
                        if (administrationMember == null) {
                            response.setStatus(500);
                            return;
                        } else {
                            session.setAttribute("user", administrationMember);
                        }
                    } else {
                        session.setAttribute("user", professor);
                    }
                } else {
                    session.setAttribute("user", student);
                }
                logger.info(String.format("user \"%s\" is logged in from %s (%s:%s)", login, request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort()));
            } else {
                request.setAttribute("message", "Имя пользователя не опознано");
                logger.info(String.format("user \"%s\" unsuccessfully tried to log in from %s (%s:%s)", login, request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort()));
            }
        } else if (request.getSession(false) == null) {
            response.setStatus(401);
        } else {
            try {
                response.getOutputStream().println((request.getSession().getAttribute("user")).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
