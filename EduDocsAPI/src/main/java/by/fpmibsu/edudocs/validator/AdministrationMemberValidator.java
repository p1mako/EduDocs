package by.fpmibsu.edudocs.validator;

import main.java.by.avelana.library.domain.Author;
import main.java.by.avelana.library.exception.IncorrectFormDataException;

import javax.servlet.http.HttpServletRequest;

public class AdministrationMemberValidator implements Validator<Author> {
	@Override
	public Author validate(HttpServletRequest request) throws IncorrectFormDataException {
		Author author = new Author();
		String parameter = request.getParameter("identity");
		if(parameter != null) {
			try {
				author.setIdentity(Integer.parseInt(parameter));
			} catch(NumberFormatException e) {
				throw new IncorrectFormDataException("identity", parameter);
			}
		}
		parameter = request.getParameter("surname");
		if(parameter != null && !parameter.isEmpty()) {
			author.setSurname(parameter);
		} else {
			throw new IncorrectFormDataException("surname", parameter);
		}
		parameter = request.getParameter("name");
		if(parameter != null && !parameter.isEmpty()) {
			author.setName(parameter);
		} else {
			throw new IncorrectFormDataException("name", parameter);
		}
		parameter = request.getParameter("patronymic");
		if(parameter != null && !parameter.isEmpty()) {
			author.setPatronymic(parameter);
		} else {
			throw new IncorrectFormDataException("patronymic", parameter);
		}
		parameter = request.getParameter("yearOfBirth");
		try {
			author.setYearOfBirth(Integer.parseInt(parameter));
		} catch(NumberFormatException e) {
			throw new IncorrectFormDataException("yearOfBirth", parameter);
		}
		parameter = request.getParameter("yearOfDeath");
		if(parameter != null && !parameter.isEmpty()) {
			try {
				author.setYearOfDeath(Integer.parseInt(parameter));
			} catch(NumberFormatException e) {
				throw new IncorrectFormDataException("yearOfDeath", parameter);
			}
		}
		return author;
	}
}
