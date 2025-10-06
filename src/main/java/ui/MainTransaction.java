package main.java.ui;

import main.java.controllers.TransactionController;
import main.java.controllers.MempoolController;
import main.java.repositories.TransactionRepository;
import main.java.repositories.implementations.TransactionRepositoryImpl;
import main.java.services.MempoolService;
import main.java.services.TransactionService;
import main.java.services.WalletService;
import main.java.services.implementations.MempoolServiceImpl;
import main.java.services.implementations.TransactionServiceImpl;
import main.java.services.implementations.WalletServiceImpl;
import main.java.repositories.implementations.WalletRepositoryImpl;
import main.java.utils.DBConnection;

import java.util.Scanner;

public class MainTransaction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        WalletRepositoryImpl walletRepository = new WalletRepositoryImpl(() -> {
            try {
                return DBConnection.getInstance().getConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        WalletService walletService = new WalletServiceImpl(walletRepository, null);

        // Service Mempool
        MempoolService mempoolService = new MempoolServiceImpl();

        // TransactionRepository
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(() -> {
            try {
                return DBConnection.getInstance().getConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // TransactionService
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository, mempoolService);

        // Controllers
        TransactionController txController = new TransactionController(transactionService, mempoolService, walletService);
        MempoolController mempoolController = new MempoolController(mempoolService);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Créer une transaction");
            System.out.println("2. Confirmer une transaction");
            System.out.println("3. Lister toutes les transactions dans le Mempool");
            System.out.println("4. Rechercher une transaction dans le Mempool");
            System.out.println("5. Supprimer une transaction du Mempool");
            System.out.println("6. Lister toutes les transactions confirmées");
            System.out.println("7. Quitter");
            System.out.print("Votre choix : ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Veuillez entrer un nombre valide !");
                continue;
            }

            switch (choice) {
                case 1:
                    txController.createTransaction();
                    break;
                case 2:
                    txController.confirmTransaction();
                    break;
                case 3:
                    mempoolController.listMempoolTransactions();
                    break;
                case 4:
                    mempoolController.getTransactionById();
                    break;
                case 5:
                    mempoolController.removeTransaction();
                    break;
                case 6:
                    txController.listConfirmedTransactions();
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("⚠️ Choix invalide !");
            }
        }

        System.out.println("👋 Programme terminé.");
        scanner.close();
    }
}
