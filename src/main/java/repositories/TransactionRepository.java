package main.java.repositories;

import main.java.models.Transaction;
import java.util.List;

public interface TransactionRepository {
    void add(Transaction transaction) throws RepositoryException;
    void update(Transaction transaction) throws RepositoryException;
    void delete(String id) throws RepositoryException;
    Transaction getById(String id) throws RepositoryException;
    List<Transaction> getBySource(String address) throws RepositoryException;
    List<Transaction> getByDestination(String address) throws RepositoryException;
    List<Transaction> getAll() throws RepositoryException;
}
