package main.java.controllers;

import main.java.models.EthereumWallet;
import main.java.models.Enums.CryptoType;
import main.java.services.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class EthereumWalletController {

    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public EthereumWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void createWallet() {
        // توليد ID تلقائي
        String id = UUID.randomUUID().toString();

        // قراءة الرصيد من المستخدم
        System.out.print("Entrer le solde initial du Ethereum Wallet: ");
        BigDecimal balance = safeReadBigDecimal();

        EthereumWallet ethWallet = new EthereumWallet(
                id,
                null,
                CryptoType.ETHEREUM,
                balance,
                LocalDateTime.now(),
                1
        );
        ethWallet.setAddress(ethWallet.generateAddress());
        walletService.createWallet(ethWallet);

        System.out.println("✅ Ethereum Wallet créé: " + ethWallet.getAddress() +
                           " | ID: " + ethWallet.getId() +
                           " | Balance: " + ethWallet.getBalance());
    }

    private BigDecimal safeReadBigDecimal() {
        BigDecimal value = null;
        while (value == null) {
            try {
                value = new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Entrée invalide, veuillez entrer un nombre valide: ");
            }
        }
        return value;
    }
}
