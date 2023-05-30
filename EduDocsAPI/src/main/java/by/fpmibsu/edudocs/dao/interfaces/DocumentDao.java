package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.Document;

import java.util.List;

public interface DocumentDao extends Dao<Document> {
    List<Document> read() throws DaoException;
}
