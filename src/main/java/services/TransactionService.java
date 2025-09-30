package main.java.services;

import main.java.models.Transaction;
import main.java.models.Enums.TransactionStatus;

import java.util.List;

public interface TransactionService {
    void createTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    Transaction getTransactionById(String id);
    List<Transaction> getAllTransactions();
    void updateTransactionStatus(String id, TransactionStatus newStatus);

}
