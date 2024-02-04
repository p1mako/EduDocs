package by.fpmibsu.edudocs.service.utils;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.interfaces.Transaction;
import by.fpmibsu.edudocs.dao.interfaces.TransactionFactory;
import by.fpmibsu.edudocs.service.*;
import by.fpmibsu.edudocs.service.interfaces.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactoryImpl implements ServiceFactory {
	private static final Logger logger = LogManager.getLogger(ServiceFactoryImpl.class);

	private static final Map<Class<? extends Service>, Class<? extends AbstractService>> SERVICES = new ConcurrentHashMap<>();

	static {
		SERVICES.put(UserService.class, UserServiceImpl.class);
		SERVICES.put(StudentService.class, StudentServiceImpl.class);
		SERVICES.put(ProfessorService.class, ProfessorServiceImpl.class);
		SERVICES.put(AdministrationMemberService.class, AdministrationMemberServiceImpl.class);
		SERVICES.put(TemplateService.class, TemplateServiceImpl.class);
		SERVICES.put(RequestService.class, RequestServiceImpl.class);
	}

	private final TransactionFactory factory;

	public ServiceFactoryImpl(TransactionFactory factory) throws DaoException {
		this.factory = factory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Type extends Service> Type getService(Class<Type> key) throws DaoException {
		Class<? extends AbstractService> value = SERVICES.get(key);
		if(value != null) {
			try {
				ClassLoader classLoader = value.getClassLoader();
				Class<?>[] interfaces = {key};
				Transaction transaction = factory.createTransaction();
				AbstractService service = value.newInstance();
				service.setTransaction(transaction);
				InvocationHandler handler = new ServiceInvocationHandlerImpl(service);
				return (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
			} catch(DaoException e) {
				throw e;
			} catch(InstantiationException | IllegalAccessException e) {
				logger.error("It is impossible to instance service class", e);
				throw new DaoException(e);
			}
		}
		return null;
	}

	@Override
	public void close() {
		factory.close();
	}
}
