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
                System.out.println("➡ " + w.getId() + " | " + w.getCryptoType() +
                        " | " + w.getAddress() + " | Balance: " + w.getBalance());
            }
        }
    }

    public void getWalletById() {
        System.out.print("Entrer l'ID du wallet: ");
        String id = scanner.nextLine();
        Wallet wallet = walletService.getWalletById(id);
        if (wallet != null) {
            System.out.println("✅ Wallet trouvé: " + wallet.getAddress() + " | Balance: " + wallet.getBalance());
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

    public class BitcoinWalletController {

        public void createBitcoinWallet() {
            System.out.print("Entrer un ID pour Bitcoin Wallet: ");
            String id = scanner.nextLine();

            BitcoinWallet btcWallet = new BitcoinWallet(
                    id,
                    null,
                    CryptoType.BITCOIN,
                    BigDecimal.ZERO,
                    LocalDateTime.now(),
                    "v1.0",
                    "mainnet"
            );
            btcWallet.setAddress(btcWallet.generateAddress());
            walletService.createWallet(btcWallet);
            System.out.println("✅ Bitcoin Wallet créé: " + btcWallet.getAddress());
        }
    }

    public class EthereumWalletController {

        public void createEthereumWallet() {
            System.out.print("Entrer un ID pour Ethereum Wallet: ");
            String id = scanner.nextLine();

            EthereumWallet ethWallet = new EthereumWallet(
                    id,
                    null,
                    CryptoType.ETHEREUM,
                    BigDecimal.ZERO,
                    LocalDateTime.now(),
                    1
            );
            ethWallet.setAddress(ethWallet.generateAddress());
            walletService.createWallet(ethWallet);
            System.out.println("✅ Ethereum Wallet créé: " + ethWallet.getAddress());
        }
    }
}
