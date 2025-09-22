package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import main.java.models.Enums.CryptoType;

public abstract class Wallet {
    
    private String id;
    private String address;
    private CryptoType cryptoType;
    private BigDecimal balance; 
    private LocalDateTime creatDate;

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



    public abstract String generateAddress();
    public abstract boolean validateAddress(String address);


    

}
