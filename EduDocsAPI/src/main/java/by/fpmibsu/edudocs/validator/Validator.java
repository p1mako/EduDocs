package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.User;

import javax.servlet.http.HttpServletRequest;

public interface Validator<Type extends User> {
	Type validate(HttpServletRequest request) throws IncorrectFormDataException;
}
