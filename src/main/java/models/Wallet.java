package main.java.models;

import java.time.LocalDateTime;

import main.java.models.Enums.CryptoType;

public abstract class Wallet {
    
    private String id;
    private String address;
    private CryptoType cryptoType;
    private double balance; 
    private LocalDateTime creatDate;

    public Wallet(String id, String address, CryptoType cryptoType, double balance, LocalDateTime creatDate) {
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
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public LocalDateTime getCreatDate() {
        return creatDate;
    }



    public abstract String generateAssres();
    public abstract boolean validateAddres(String address);


    

}
