package main.java.controllers;

import main.java.models.EthereumWallet;
import main.java.models.Enums.CryptoType;
import main.java.services.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class EthereumWalletController {

    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public EthereumWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void createWallet() {
        System.out.print("Entrer un ID pour Ethereum Wallet: ");
        String id = scanner.nextLine();

        EthereumWallet ethWallet = new EthereumWallet(
                id, null, CryptoType.ETHEREUM, BigDecimal.ZERO,
                LocalDateTime.now(), 1
        );
        ethWallet.setAddress(ethWallet.generateAddress());
        walletService.createWallet(ethWallet);
        System.out.println("✅ Ethereum Wallet créé: " + ethWallet.getAddress());
    }
}
