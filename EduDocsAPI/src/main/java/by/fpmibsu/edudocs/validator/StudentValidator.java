package by.fpmibsu.edudocs.validator;

import main.java.by.avelana.library.domain.Reader;
import main.java.by.avelana.library.exception.IncorrectFormDataException;

import javax.servlet.http.HttpServletRequest;

public class StudentValidator implements Validator<Reader> {
	@Override
	public Reader validate(HttpServletRequest request) throws IncorrectFormDataException {
		Reader reader = new Reader();
		String parameter = request.getParameter("identity");
		if(parameter != null) {
			try {
				reader.setIdentity(Integer.parseInt(parameter));
			} catch(NumberFormatException e) {
				throw new IncorrectFormDataException("identity", parameter);
			}
		}
		parameter = request.getParameter("libraryCardNumber");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setLibraryCardNumber(parameter);
		} else {
			throw new IncorrectFormDataException("libraryCardNumber", parameter);
		}
		parameter = request.getParameter("surname");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setSurname(parameter);
		} else {
			throw new IncorrectFormDataException("surname", parameter);
		}
		parameter = request.getParameter("name");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setName(parameter);
		} else {
			throw new IncorrectFormDataException("name", parameter);
		}
		parameter = request.getParameter("patronymic");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setPatronymic(parameter);
		} else {
			throw new IncorrectFormDataException("patronymic", parameter);
		}
		parameter = request.getParameter("address");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setAddress(parameter);
		} else {
			throw new IncorrectFormDataException("address", parameter);
		}
		parameter = request.getParameter("phone");
		if(parameter != null && !parameter.isEmpty()) {
			reader.setPhone(parameter);
		} else {
			throw new IncorrectFormDataException("phone", parameter);
		}
		return reader;
	}
}
