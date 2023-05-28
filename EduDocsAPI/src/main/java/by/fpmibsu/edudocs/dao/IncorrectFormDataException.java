package by.fpmibsu.edudocs.dao;

public class IncorrectFormDataException extends Exception {
	public IncorrectFormDataException(String param, String value) {
		super(String.format("Empty or incorrect \"%s\" parameter was found: %s", param, value));
	}
}
