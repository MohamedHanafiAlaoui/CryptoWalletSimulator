package main.java.services;

import main.java.models.Transaction;
import java.util.List;

public interface MempoolService {  // ✅ interface وليس class

    void addToMempool(Transaction transaction);

    void removeFromMempool(String transactionId);

    Transaction getTransactionById(String transactionId);

    List<Transaction> getAllTransactions();
}
