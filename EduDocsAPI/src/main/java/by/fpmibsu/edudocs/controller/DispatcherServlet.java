package by.fpmibsu.edudocs.controller;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.action.Action;
import by.fpmibsu.edudocs.action.ActionManager;
import by.fpmibsu.edudocs.action.ActionManagerFactory;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.TransactionFactoryImpl;
import by.fpmibsu.edudocs.dao.pool.ConnectionPool;
import by.fpmibsu.edudocs.service.utils.ServiceFactory;
import by.fpmibsu.edudocs.service.utils.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class DispatcherServlet  extends HttpServlet  {
    private static Logger logger = LogManager.getLogger(DispatcherServlet.class);

    public static final String LOG_FILE_NAME = "log.txt";
    public static final Level LOG_LEVEL = Level.ALL;
    public static final String LOG_MESSAGE_FORMAT = "%n%d%n%p\t%C.%M:%L%n%m%n";

    public static Properties prop = new Properties();
    public static final String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/EduDocs?useUnicode=true&characterEncoding=UTF-8";
    public static final String DB_USER = "orlovich";
    public static final String DB_PASSWORD = "library_password";
    public static final int DB_POOL_START_SIZE = 10;
    public static final int DB_POOL_MAX_SIZE = 1000;
    public static final int DB_POOL_CHECK_CONNECTION_TIMEOUT = 0;

    public void init() {
        try {
            InputStream stream = App.class.getClassLoader().getResourceAsStream("config.properties");
            try {
                prop.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            ConnectionPool.getInstance().init(DB_DRIVER_CLASS, DB_URL, prop , DB_POOL_START_SIZE, DB_POOL_MAX_SIZE, DB_POOL_CHECK_CONNECTION_TIMEOUT);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    public ServiceFactory getFactory() throws DaoException {
        return new ServiceFactoryImpl(new TransactionFactoryImpl());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        Action action = (Action)request.getAttribute("action");
        try {
            HttpSession session = request.getSession(false);
            if(session != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> attributes = (Map<String, Object>)session.getAttribute("redirectedData");
                if(attributes != null) {
                    for(String key : attributes.keySet()) {
                        request.setAttribute(key, attributes.get(key));
                    }
                    session.removeAttribute("redirectedData");
                }
            }

            ActionManager actionManager = ActionManagerFactory.getManager(getFactory());
            Action.Forward forward = actionManager.execute(action, request, response);
            actionManager.close();
            if(session != null && forward != null && !forward.getAttributes().isEmpty()) {
                session.setAttribute("redirectedData", forward.getAttributes());
            }
            String requestedUri = request.getRequestURI();
            if(forward != null && forward.isRedirect()) {
                String redirectedUri = request.getContextPath() + forward.getForward();
                logger.debug(String.format("Request for URI \"%s\" id redirected to URI \"%s\"", requestedUri, redirectedUri));
                response.sendRedirect(redirectedUri);
            } else {
                String jspPage;
                if(forward != null) {
                    jspPage = forward.getForward();
                } else {
                    jspPage = action.getName() + ".jsp";
                }
                jspPage = "/WEB-INF/jsp" + jspPage;
                logger.debug(String.format("Request for URI \"%s\" is forwarded to JSP \"%s\"", requestedUri, jspPage));
                getServletContext().getRequestDispatcher(jspPage).forward(request, response);
            }
        } catch(DaoException e) {
            logger.error("It is impossible to process request", e);
            request.setAttribute("error", "Ошибка обработки данных");
            getServletContext().getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
