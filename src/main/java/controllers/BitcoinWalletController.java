package main.java.controllers;

import main.java.models.BitcoinWallet;
import main.java.models.Enums.CryptoType;
import main.java.services.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class BitcoinWalletController {

    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public BitcoinWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void createWallet() {
        System.out.print("Entrer un ID pour Bitcoin Wallet: ");
        String id = scanner.nextLine();

        BitcoinWallet btcWallet = new BitcoinWallet(
                id, null, CryptoType.BITCOIN, BigDecimal.ZERO,
                LocalDateTime.now(), "v1.0", "mainnet"
        );
        btcWallet.setAddress(btcWallet.generateAddress());
        walletService.createWallet(btcWallet);
        System.out.println("✅ Bitcoin Wallet créé: " + btcWallet.getAddress());
    }
}
