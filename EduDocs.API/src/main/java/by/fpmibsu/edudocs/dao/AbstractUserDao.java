package by.fpmibsu.edudocs.dao;

import by.fpmibsu.edudocs.entities.User;

import java.util.UUID;

public abstract class AbstractUserDao extends AbstractDao<User> {
    public UUID GetUUIDByLogin(){
        return UUID.randomUUID();
    }
}
