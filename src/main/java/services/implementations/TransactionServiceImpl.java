package main.java.services.implementations;

import main.java.models.Transaction;
import main.java.models.Enums.TransactionStatus;
import main.java.repositories.TransactionRepository;
import main.java.repositories.RepositoryException;
import main.java.services.TransactionService;
import main.java.services.MempoolService;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final MempoolService mempoolService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, MempoolService mempoolService) {
        this.transactionRepository = transactionRepository;
        this.mempoolService = mempoolService;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        mempoolService.addToMempool(transaction);

        try {
            transactionRepository.add(transaction);
            mempoolService.removeFromMempool(transaction.getId());
        } catch (RepositoryException e) {
            System.err.println("❌ Failed to persist transaction: " + e.getMessage());
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        transactionRepository.update(transaction);
    }

    @Override
    public Transaction getTransactionById(String id) {
        return transactionRepository.getById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAll();
    }

    @Override
    public void updateTransactionStatus(String id, TransactionStatus newStatus) {
        Transaction tx = transactionRepository.getById(id);
        if (tx != null) {
            tx.setStatus(newStatus);
            updateTransaction(tx);
            System.out.println("✅ Status updated: " + id + " -> " + newStatus);
        } else {
            System.out.println("⚠️ Transaction not found: " + id);
        }
    }

}
