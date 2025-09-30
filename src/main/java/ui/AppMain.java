package main.java.ui;

import java.util.Scanner;

public class AppMain {

    public  void start() {
        Scanner scanner = new Scanner(System.in);
        int choix = -1;

        do {
            System.out.println("\n===== APPLICATION CRYPTO =====");
            System.out.println("1. Gestion des Wallets");
            System.out.println("2. Gestion des Transactions & Mempool");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            try {
                
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Veuillez entrer un nombre valide !");
                continue; 
            }

            switch (choix) {
                case 1:
                    MainWallet.main(new String[]{});
                    break;
                case 2:
                    MainTransaction.main(new String[]{});
                    break;
                case 0:
                    System.out.println(" Fermeture de l'application.");
                    break;
                default:
                    System.out.println(" Choix invalide !");
            }

        } while (choix != 0);

        scanner.close();
    }
}
