package by.fpmibsu.edudocs.action;

import by.fpmibsu.edudocs.service.utils.ServiceFactory;

public class ActionManagerFactory {
    public static ActionManager getManager(ServiceFactory factory) {
        return new ActionManagerImpl(factory);
    }
}

