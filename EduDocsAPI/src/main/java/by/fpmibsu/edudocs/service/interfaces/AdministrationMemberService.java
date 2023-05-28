package by.fpmibsu.edudocs.service.interfaces;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.entities.AdministrationMember;

import java.util.UUID;

public interface AdministrationMemberService extends Service {
    AdministrationMember findByIdentity(UUID identity) throws DaoException;
}
