package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.StudentDaoImpl;
import by.fpmibsu.edudocs.dao.interfaces.AdministrationMemberDao;
import by.fpmibsu.edudocs.entities.AdministrationMember;
import by.fpmibsu.edudocs.service.interfaces.AdministrationMemberService;
import by.fpmibsu.edudocs.service.utils.AbstractService;

import java.util.UUID;

public class AdministrationMemberServiceImpl extends AbstractService implements AdministrationMemberService {
    @Override
    public AdministrationMember findByIdentity(UUID identity) throws DaoException {
        var dao = transaction.createDao(AdministrationMemberDao.class);
        return dao.read(identity);
    }
}
