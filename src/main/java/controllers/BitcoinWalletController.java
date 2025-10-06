package main.java.controllers;

import main.java.models.BitcoinWallet;
import main.java.models.Enums.CryptoType;
import main.java.services.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class BitcoinWalletController {

    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public BitcoinWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void createWallet() {
        // توليد ID تلقائي
        String id = UUID.randomUUID().toString();

        // قراءة الرصيد من المستخدم
        System.out.print("Entrer le solde initial du Bitcoin Wallet: ");
        BigDecimal balance = safeReadBigDecimal();

        BitcoinWallet btcWallet = new BitcoinWallet(
                id,
                null, 
                CryptoType.BITCOIN,
                balance,
                LocalDateTime.now(),
                "v1.0",
                "mainnet"
        );
        btcWallet.setAddress(btcWallet.generateAddress());
        walletService.createWallet(btcWallet);

        System.out.println("✅ Bitcoin Wallet créé: " + btcWallet.getAddress() +
                           " | ID: " + btcWallet.getId() +
                           " | Balance: " + btcWallet.getBalance());
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
