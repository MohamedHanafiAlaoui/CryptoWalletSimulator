package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import main.java.models.Enums.CryptoType;

public abstract class Wallet {

    private String id;
    private String address;
    private CryptoType cryptoType;
    private BigDecimal balance;
    private LocalDateTime creatDate;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Wallet(String id, String address, CryptoType cryptoType, BigDecimal balance, LocalDateTime creatDate) {
        this.id = id;
        this.address = address;
        this.cryptoType = cryptoType;
        this.balance = balance;
        this.creatDate = creatDate;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public CryptoType getCryptoType() {
        return cryptoType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatDate() {
        return creatDate;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    

    public abstract String generateAddress();

    public abstract boolean validateAddress(String address);

    public void sendTransaction(Wallet receiver, BigDecimal amount, Transaction tx) {
        BigDecimal fees = tx.calculateFees();
        this.balance = this.balance.subtract(amount.add(fees));
        receiver.setBalance(receiver.getBalance().add(amount));
        this.transactions.add(tx);
        receiver.getTransactions().add(tx);

    }

}
