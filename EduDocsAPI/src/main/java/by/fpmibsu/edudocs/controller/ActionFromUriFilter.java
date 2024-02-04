package by.fpmibsu.edudocs.controller;


import by.fpmibsu.edudocs.action.Action;
import by.fpmibsu.edudocs.action.LoginAction;
import by.fpmibsu.edudocs.action.LogoutAction;
import by.fpmibsu.edudocs.action.TemplateAction;
import by.fpmibsu.edudocs.action.admin.UserDeleteAction;
import by.fpmibsu.edudocs.action.requests.RequestCreateAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebFilter(filterName = "asb", asyncSupported = true, urlPatterns = {"/*"})
public class ActionFromUriFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(ActionFromUriFilter.class);

    private static Map<String, Class<? extends Action>> actions = new ConcurrentHashMap<>();

    static {
        actions.put("/", LoginAction.class);
        actions.put("/user/create", LoginAction.class);
        actions.put("/user/delete", UserDeleteAction.class);
        actions.put("/logout", LogoutAction.class);
        actions.put("/login", LoginAction.class);
        actions.put("/templates", TemplateAction.class);
        actions.put("/request/create", RequestCreateAction.class);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String contextPath = httpRequest.getContextPath();
            String uri = httpRequest.getRequestURI();
            logger.debug(String.format("Starting of processing of request for URI \"%s\"", uri));
            int beginAction = contextPath.length();
            int endAction = uri.lastIndexOf('.');
            String actionName;
            if (endAction >= 0) {
                actionName = uri.substring(beginAction, endAction);
            } else {
                actionName = uri.substring(beginAction);
            }

            Class<? extends Action> actionClass = actions.get(actionName);
            try {
                Action action = actionClass.newInstance();
                action.setName(actionName);
                httpRequest.setAttribute("action", action);
                chain.doFilter(request, response);
            } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
                logger.error("It is impossible to create action handler object", e);
                httpRequest.setAttribute("error", String.format("Запрошенный адрес %s не может быть обработан сервером", uri));
            }
        } else {
            logger.error("It is impossible to use HTTP filter");
        }
    }

    @Override
    public void destroy() {
    }

}
