package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ValidatorFactory {
	private static final Map<Class<? extends Entity>, Class<? extends Validator<?>>> validators = new HashMap<>();

	static {
		validators.put(AdministrationMember.class, AdministrationMemberValidator.class);
		validators.put(Professor.class, ProfessorValidator.class);
		validators.put(Student.class, StudentValidator.class);
		validators.put(User.class, UserValidator.class);
		validators.put(Request.class, RequestValidator.class);
	}

	@SuppressWarnings("unchecked")
	public static <Type extends Entity> Validator<Type> createValidator(Class<Type> entity) {
		try {
			return (Validator<Type>)validators.get(entity).getDeclaredConstructor().newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
