package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.service.StudentServiceImpl;
import by.fpmibsu.edudocs.service.interfaces.AdministrationMemberService;
import by.fpmibsu.edudocs.service.interfaces.ProfessorService;
import by.fpmibsu.edudocs.service.interfaces.StudentService;
import by.fpmibsu.edudocs.service.interfaces.UserService;
import by.fpmibsu.edudocs.service.utils.ServiceFactory;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;
import by.fpmibsu.edudocs.service.utils.ServiceInvocationHandler;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class LoginAction extends Action {

    private static final Logger logger = LogManager.getLogger(LoginAction.class);

    private String tryGetStudent(UUID id, String password) throws DaoException, JsonProcessingException {
        var service = factory.getService(StudentService.class);
        var student = service.findByIdentity(id);
        if (student != null){
            if (Objects.equals(student.getPassword(), password)){
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(student);
            }
        }
        return null;
    }

    private String tryGetProfessor(UUID id, String password) throws DaoException, JsonProcessingException {
        var service = factory.getService(ProfessorService.class);
        var professor=  service.findByIdentity(id);
        if (professor != null){
            if (Objects.equals(professor.getPassword(), password)){
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(professor);
            }
        }
        return null;
    }

    private String tryGetAdmin(UUID id, String password) throws DaoException, JsonProcessingException {
        var service = factory.getService(AdministrationMemberService.class);
        var admin = service.findByIdentity(id);
        if (admin != null){
            if (Objects.equals(admin.getPassword(), password)){
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(admin);
            }
        }
        return null;
    }

    @Override
    public void exec(HttpServletRequest request, HttpServletResponse response) throws DaoException{
        String login = null;
        String password = null;
        try {
            String toParse = request.getReader().readLine();
            if (toParse != null) {
                ObjectMapper mapper = new ObjectMapper();
                var tree = mapper.readTree(toParse);
                login = tree.get("login").textValue();
                password = tree.get("password").textValue();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Info: " + login + " " + password);
        if (login != null && password != null) {
            UserService service = factory.getService(UserService.class);
            User user = service.findByLogin(login);
            if (user != null) {
                HttpSession session = request.getSession();
                String student, professor, administrationMember;
                try {
                    student = tryGetStudent(user.getId(), user.getPassword());
                    if (student == null) {
                        professor = tryGetProfessor(user.getId(), user.getPassword());
                        if (professor == null) {
                            administrationMember = tryGetAdmin(user.getId(), user.getPassword());
                            if (administrationMember == null) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                return;
                            }
                            response.getOutputStream().println(administrationMember);
                            session.setAttribute("user", administrationMember);
                            return;
                        }
                        response.getOutputStream().println(professor);
                        session.setAttribute("user", professor);
                        return;
                    }
                    response.getOutputStream().println(student);
                    session.setAttribute("user", student);
                } catch (IOException ee){
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                logger.info(String.format("user \"%s\" is logged in from %s (%s:%s)", login, request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort()));
            } else {
                request.setAttribute("message", "Имя пользователя не опознано");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
