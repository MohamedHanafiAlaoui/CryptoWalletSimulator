package main.java.services;

import main.java.models.Wallet;
import main.java.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    void createWallet(Wallet wallet);
    Wallet getWalletById(String id);
    Wallet getWalletByAddress(String address);
    List<Wallet> getAllWallets();

    void sendTransaction(Wallet sender, Wallet receiver, BigDecimal amount, Transaction tx);
    void deleteWallet(String id);
        void updateWallet(Wallet wallet);

}
