package main.java.services.implementations;

import main.java.models.Wallet;
import main.java.models.Transaction;
import main.java.repositories.WalletRepository;
import main.java.services.WalletService;
import main.java.services.TransactionService;

import java.math.BigDecimal;
import java.util.List;

public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;

    public WalletServiceImpl(WalletRepository walletRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
    }

    @Override
    public void createWallet(Wallet wallet) {
        walletRepository.add(wallet);
    }

    @Override
    public Wallet getWalletById(String id) {
        return walletRepository.getById(id);
    }

    @Override
    public Wallet getWalletByAddress(String address) {
        return walletRepository.getByAddress(address);
    }

    @Override
    public List<Wallet> getAllWallets() {
        return walletRepository.getAll();
    }

    @Override
    public void sendTransaction(Wallet sender, Wallet receiver, BigDecimal amount, Transaction tx) {
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("❌ Solde insuffisant dans le portefeuille !");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        walletRepository.update(sender);

        receiver.setBalance(receiver.getBalance().add(amount));
        walletRepository.update(receiver);

        transactionService.createTransaction(tx);
    }

    @Override
    public void deleteWallet(String id) {
        walletRepository.delete(id);
    }

    @Override
    public void updateWallet(Wallet wallet) {
        walletRepository.update(wallet);
    }

}
