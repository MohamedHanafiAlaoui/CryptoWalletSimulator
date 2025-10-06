package main.java.controllers;

import main.java.models.BitcoinWallet;
import main.java.models.EthereumWallet;
import main.java.models.Wallet;
import main.java.models.Enums.CryptoType;
import main.java.services.WalletService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class WalletController {

    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    public void listWallets() {
        List<Wallet> wallets = walletService.getAllWallets();
        if (wallets.isEmpty()) {
            System.out.println("⚠️ Aucun wallet trouvé.");
        } else {
            for (Wallet w : wallets) {
                System.out.println("➡ ID: " + w.getId() +
                                   " | Type: " + w.getCryptoType() +
                                   " | Address: " + w.getAddress() +
                                   " | Balance: " + w.getBalance());
            }
        }
    }

    public void getWalletById() {
        System.out.print("Entrer l'ID du wallet: ");
        String id = scanner.nextLine();
        Wallet wallet = walletService.getWalletById(id);
        if (wallet != null) {
            System.out.println("✅ Wallet trouvé: " + wallet.getAddress() +
                               " | Balance: " + wallet.getBalance());
        } else {
            System.out.println("❌ Wallet introuvable.");
        }
    }

    public void deleteWallet() {
        System.out.print("Entrer l'ID du wallet à supprimer: ");
        String id = scanner.nextLine();
        walletService.deleteWallet(id);
        System.out.println("🗑 Wallet supprimé.");
    }

    public void createBitcoinWallet() {
        
        BigDecimal balance = safeReadBigDecimal();

        String id = UUID.randomUUID().toString(); //
        BitcoinWallet btcWallet = new BitcoinWallet(
                id,
                null, // 
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

    public void createEthereumWallet() {
        System.out.print("Entrer le balance initial: ");
        BigDecimal balance = safeReadBigDecimal();

        String id = UUID.randomUUID().toString(); 
        EthereumWallet ethWallet = new EthereumWallet(
                id,
                null, 
                CryptoType.ETHEREUM,
                balance,
                LocalDateTime.now(),
                1 // رقم الإصدار أو أي حقل مخصص
        );
        ethWallet.setAddress(ethWallet.generateAddress()); // توليد Address تلقائي
        walletService.createWallet(ethWallet);
        System.out.println("✅ Ethereum Wallet créé: " + ethWallet.getAddress() +
                           " | ID: " + ethWallet.getId() +
                           " | Balance: " + ethWallet.getBalance());
    }

    private BigDecimal safeReadBigDecimal() {
        BigDecimal value = null;
        while (value == null) {
            String line = scanner.nextLine();
            try {
                value = new BigDecimal(line);
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Entrée invalide, veuillez entrer un nombre valide: ");
            }
        }
        return value;
    }
}
