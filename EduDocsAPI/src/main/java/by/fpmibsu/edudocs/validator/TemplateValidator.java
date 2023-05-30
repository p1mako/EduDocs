package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.dao.IncorrectFormDataException;
import by.fpmibsu.edudocs.entities.Request;
import by.fpmibsu.edudocs.entities.Template;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class TemplateValidator implements Validator<Template> {
    @Override
    public Template validate(HttpServletRequest request) throws IncorrectFormDataException, IOException {
        return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), Template.class);
    }
}
