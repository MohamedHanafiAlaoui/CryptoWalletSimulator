package main.java;

import main.java.controllers.WalletController;
import main.java.services.WalletService;
import main.java.services.implementations.WalletServiceImpl;
import main.java.repositories.WalletRepository;

import java.util.Scanner;

public class MainWallet {
    public static void main(String[] args) {
        WalletRepository walletRepository = new WalletRepository();
        WalletService walletService = new WalletServiceImpl(walletRepository, null);
        WalletController walletController = new WalletController(walletService);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Wallet Menu ---");
            System.out.println("1. Créer Wallet");
            System.out.println("2. Lister Wallets");
            System.out.println("3. Supprimer Wallet");
            System.out.println("4. Quitter");
            System.out.print("Choisir une option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> walletController.createWallet();
                case 2 -> walletController.listWallets();
                case 3 -> walletController.deleteWallet();
                case 4 -> System.out.println("👋 Au revoir !");
                default -> System.out.println("❌ Option invalide !");
            }
        } while (choice != 4);
    }
}
