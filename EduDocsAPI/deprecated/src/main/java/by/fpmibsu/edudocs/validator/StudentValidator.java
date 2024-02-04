package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class StudentValidator implements Validator<Student> {
    @Override
    public Student validate(HttpServletRequest request) throws IOException {
        return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), Student.class);
    }
}
