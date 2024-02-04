package by.fpmibsu.edudocs.dao.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;

import java.util.List;

public interface AdministrationMemberDao extends Dao<AdministrationMember> {
    List<AdministrationMember> read() throws DaoException;
}
