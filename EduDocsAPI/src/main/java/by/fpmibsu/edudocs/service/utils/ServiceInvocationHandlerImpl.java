package by.fpmibsu.edudocs.service.utils;

import by.fpmibsu.edudocs.dao.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceInvocationHandlerImpl implements InvocationHandler {
    private static final Logger logger = LogManager.getLogger(ServiceInvocationHandlerImpl.class);

    private final ServiceImpl service;

    public ServiceInvocationHandlerImpl(ServiceImpl service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        try {
            Object result = method.invoke(service, arguments);
            service.transaction.commit();
            return result;
        } catch(DaoException e) {
            rollback(method);
            throw e;
        } catch(InvocationTargetException e) {
            rollback(method);
            throw e.getCause();
        }
    }

    private void rollback(Method method) {
        try {
            service.transaction.rollback();
        } catch(DaoException e) {
            logger.warn("It is impossible to rollback transaction", e);
        }
    }
}
