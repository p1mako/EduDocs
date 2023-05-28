package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.entities.Professor;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.User;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ValidatorFactory {
	private static final Map<Class<? extends User>, Class<? extends Validator<?>>> validators = new HashMap<>();

	static {
		validators.put(AdministrationMember.class, AdministrationMemberValidator.class);
		validators.put(Professor.class, ProfessorValidator.class);
		validators.put(Student.class, StudentValidator.class);
		validators.put(User.class, UserValidator.class);
	}

	@SuppressWarnings("unchecked")
	public static <Type extends User> Validator<Type> createValidator(Class<Type> entity) {
		try {
			return (Validator<Type>)validators.get(entity).getDeclaredConstructor().newInstance();
		} catch(InstantiationException | IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
