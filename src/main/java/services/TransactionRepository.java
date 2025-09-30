package main.java.services;

import main.java.models.Transaction;
import java.util.List;

public interface TransactionRepository {
    void add(Transaction transaction) ;
    void update(Transaction transaction) ;
    Transaction getById(String id) ;
    List<Transaction> getAll() ;
    void delete(String id) ;
}
