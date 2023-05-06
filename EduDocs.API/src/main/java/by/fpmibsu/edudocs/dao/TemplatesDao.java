package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.Entity;
import by.fpmibsu.edudocs.entities.Template;

import java.util.List;
import java.util.UUID;

public class TemplatesDao extends AbstractDao<Template> {
    @Override
    public List<Template> findAll() {
        return null;
    }

    @Override
    public Template findEntityById(UUID id) {
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public boolean delete(Template entity) {
        return false;
    }

    @Override
    public boolean create(Template entity) {
        return false;
    }

    @Override
    public Template update(Template entity) {
        return null;
    }
}
