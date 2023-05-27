package by.fpmibsu.edudocs.service;

import by.fpmibsu.edudocs.dao.interfaces.Transaction;

abstract public class ServiceImpl implements Service {
    protected Transaction transaction = null;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
