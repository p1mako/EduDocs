package by.fpmibsu.edudocs.validator;

import by.fpmibsu.edudocs.entities.AdministrationMember;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class AdministrationMemberValidator implements Validator<AdministrationMember> {
	@Override
	public AdministrationMember validate(HttpServletRequest request) throws IOException {
		return (new ObjectMapper()).readValue(request.getReader().lines().collect(Collectors.joining(" ")), AdministrationMember.class);
	}
}
