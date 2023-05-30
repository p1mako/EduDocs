package by.fpmibsu.edudocs.service.utils;

import by.fpmibsu.edudocs.dao.interfaces.Transaction;
import by.fpmibsu.edudocs.service.interfaces.Service;

abstract public class AbstractService implements Service {
    protected Transaction transaction = null;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
