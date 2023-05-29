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
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "Dispatcher", value = "/")
public class DispatcherServlet  extends HttpServlet  {
    private static Logger logger = LogManager.getLogger(DispatcherServlet.class);

    public static final String LOG_FILE_NAME = "log.txt";
    public static final Level LOG_LEVEL = Level.ALL;
    public static final String LOG_MESSAGE_FORMAT = "%n%d%n%p\t%C.%M:%L%n%m%n";

    public static Properties prop = new Properties();
    public static final String DB_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String DB_URL = "jdbc:sqlserver://localhost";
    public static final int DB_POOL_START_SIZE = 10;
    public static final int DB_POOL_MAX_SIZE = 1000;
    public static final int DB_POOL_CHECK_CONNECTION_TIMEOUT = 0;

    public void init() {
//        Logger root = Logger.getRootLogger();
//        Layout layout = new PatternLayout(LOG_MESSAGE_FORMAT);
//        root.addAppender(new FileAppender(layout, LOG_FILE_NAME, true));
//        root.addAppender(new ConsoleAppender(layout));
//        root.setLevel(LOG_LEVEL);
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
            actionManager.execute(action, request, response);
            actionManager.close();
        } catch(DaoException e) {
            logger.error("It is impossible to process request", e);
            request.setAttribute("error", "Ошибка обработки данных");
            getServletContext().getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
