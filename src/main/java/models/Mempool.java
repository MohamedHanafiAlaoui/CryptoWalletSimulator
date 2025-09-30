package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

public class Mempool {
    private ArrayList<Transaction> transactions;
    private int capacity;

    public Mempool(int capacity) {
        this.capacity = capacity;
        this.transactions = new ArrayList<>();
    }

    public boolean addTransaction(Transaction transaction) {
        if (transactions.size() < capacity) {
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public void sortByFees() {
        transactions.sort(Comparator.comparing(Transaction::getFees).reversed());
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public int getCapacity() {
        return capacity;
    }

public int generateRandomTransactions(int count) {
    int added = 0;
    for (int i = 0; i < count; i++) {

        Transaction tx;
        if (i % 2 == 0) {
            // Bitcoin transaction
            tx = new BitcoinTransaction(
                "btc-" + i,
                "source-" + i,
                "dest-" + i,
                BigDecimal.valueOf(Math.random() * 10),
                BigDecimal.valueOf(Math.random() * 0.01),
                LocalDateTime.now(),
                TransactionStatus.PENDING,
                FeePriority.STANDARD,
                CryptoType.BITCOIN,
                "250",
                "10"
            );
        } else {
            // Ethereum transaction
            tx = new EthereumTransaction(
                "eth-" + i,
                "source-" + i,
                "dest-" + i,
                BigDecimal.valueOf(Math.random() * 10),
                BigDecimal.valueOf(Math.random() * 0.01),
                LocalDateTime.now(),
                TransactionStatus.PENDING,
                FeePriority.STANDARD,
                CryptoType.ETHEREUM,
                "21000",
                "50"
            );
        }

        if (addTransaction(tx)) {
            added++;
        }
    }
    return added;
}

}
