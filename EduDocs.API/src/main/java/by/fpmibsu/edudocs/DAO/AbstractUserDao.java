package by.fpmibsu.edudocs.DAO;

import by.fpmibsu.edudocs.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractUserDao extends AbstractDao<User> {
    public UUID GetUUIDByLogin(){
        return UUID.randomUUID();
    }
}
