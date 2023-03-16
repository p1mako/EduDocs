package by.fpmibsu.edudocs.entities;

import by.fpmibsu.edudocs.entities.utils.AdministrationRole;

import java.sql.Timestamp;

public class AdministrationMember extends User{
    AdministrationRole role;
    Timestamp from;
    Timestamp until;
}
