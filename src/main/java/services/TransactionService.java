package main.java.services;

import main.java.models.Transaction;
import java.util.List;

public interface TransactionService {
    void createTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    Transaction getTransactionById(String id);
    List<Transaction> getAllTransactions();
}
