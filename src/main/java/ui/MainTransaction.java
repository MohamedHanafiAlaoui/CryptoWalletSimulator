package main.java.ui;

import main.java.controllers.TransactionController;
import main.java.controllers.MempoolController;
import main.java.services.MempoolService;
import main.java.services.implementations.MempoolServiceImpl;

import java.util.Scanner;

public class MainTransaction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Service Mempool
        MempoolService mempoolService = new MempoolServiceImpl();

        // TransactionController nécessite TransactionService, ici on passe null pour l'instant
        TransactionController txController = new TransactionController(null, mempoolService);
        MempoolController mempoolController = new MempoolController(mempoolService);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Créer une transaction");
            System.out.println("2. Confirmer une transaction");
            System.out.println("3. Lister toutes les transactions dans le Mempool");
            System.out.println("4. Rechercher une transaction dans le Mempool");
            System.out.println("5. Supprimer une transaction du Mempool");
            System.out.println("6. Quitter");
            System.out.print("Votre choix : ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine()); // Lecture sécurisée
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Veuillez entrer un nombre valide !");
                continue; // Retour au début de la boucle
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
