package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.Professor;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class ProfessorValidator implements Validator<Professor> {
    @Override
    public Professor validate(HttpServletRequest request) throws IOException {
        return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), Professor.class);
    }
}
