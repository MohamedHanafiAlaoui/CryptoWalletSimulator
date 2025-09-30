package main.java.services.implementations;

import main.java.models.Transaction;
import main.java.services.MempoolService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MempoolServiceImpl implements MempoolService {

    private final ConcurrentHashMap<String, Transaction> mempool = new ConcurrentHashMap<>();

    @Override
    public void addToMempool(Transaction transaction) {
        mempool.put(transaction.getId(), transaction);
    }

    @Override
    public void removeFromMempool(String transactionId) {
        mempool.remove(transactionId);
    }

    @Override
    public Transaction getTransactionById(String transactionId) {
        return mempool.get(transactionId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(mempool.values());
    }
}
