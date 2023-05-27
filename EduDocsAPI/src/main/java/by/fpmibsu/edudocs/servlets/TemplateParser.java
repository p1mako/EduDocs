package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.entities.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TemplateParser {
    public TemplateParser() {
    }

    public List<Template> ListFromString(String inputString) {
        ArrayList<Template> templates = new ArrayList<>();

        String preparedString = inputString.replaceAll("[\\[\\]{}=',|]", " ");
        String regex = "\\bTemplate\\b|\\bname\\b|\\bid\\b|\\brouteToDocument\\b";
        preparedString = preparedString.replaceAll(regex, "");
        Scanner scanner = new Scanner(preparedString);

        while (scanner.hasNext()) {
            String name = scanner.next();
            String route = scanner.next();
            String id = scanner.next();
            templates.add(new Template(UUID.fromString(id), name, route));
        }
        return templates;
    }
}

