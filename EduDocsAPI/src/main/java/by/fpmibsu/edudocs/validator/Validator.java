package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.Entity;
import by.fpmibsu.edudocs.entities.User;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface Validator<Type extends Entity> {
	Type validate(HttpServletRequest request) throws IncorrectFormDataException, IOException;
}
