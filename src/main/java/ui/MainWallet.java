package main.java.ui;

import main.java.controllers.BitcoinWalletController;
import main.java.controllers.EthereumWalletController;
import main.java.models.Wallet;
import main.java.services.WalletService;
import main.java.services.implementations.WalletServiceImpl;
import main.java.repositories.implementations.WalletRepositoryImpl;
import main.java.utils.DBConnection;

import java.util.Scanner;
import java.util.List;

public class MainWallet {

    public static void main(String[] args) {

        // Création du Repository et du Service
        WalletRepositoryImpl walletRepository = new WalletRepositoryImpl(() -> {
            try {
                return DBConnection.getInstance().getConnection();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        WalletService walletService = new WalletServiceImpl(walletRepository, null);

        // Création des Controllers
        BitcoinWalletController btcController = new BitcoinWalletController(walletService);
        EthereumWalletController ethController = new EthereumWalletController(walletService);

        Scanner scanner = new Scanner(System.in);
        int  choix = -1;

        do {
            System.out.println("\n===== Menu Wallet =====");
            System.out.println("1. Créer un nouveau Wallet");
            System.out.println("2. Afficher tous les Wallets");
            System.out.println("3. Rechercher un Wallet par ID");
            System.out.println("4. Supprimer un Wallet");
            System.out.println("0. Quitter");
            System.out.print("Votre choix: ");

            try {
                choix = Integer.parseInt(scanner.nextLine()); // lecture sécurisée
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Veuillez entrer un nombre valide !");
                continue; // revenir au début de la boucle
            }

            switch (choix) {
                case 1:
                    System.out.println("🔹 Choisir le type de Wallet:");
                    System.out.println("1. Bitcoin Wallet");
                    System.out.println("2. Ethereum Wallet");
                    int type;
                    try {
                        type = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Veuillez entrer un nombre valide !");
                        break;
                    }
                    if (type == 1) btcController.createWallet();
                    else if (type == 2) ethController.createWallet();
                    else System.out.println("❌ Choix invalide !");
                    break;

                case 2:
                    List<Wallet> wallets = walletService.getAllWallets();
                    for (Wallet w : wallets) {
                        System.out.println("➡ ID: " + w.getId() +
                                " | Type: " + w.getCryptoType() +
                                " | Adresse: " + w.getAddress() +
                                " | Solde: " + w.getBalance());
                    }
                    break;

                case 3:
                    System.out.print("Entrer l'ID du Wallet: ");
                    String id = scanner.nextLine();
                    Wallet wallet = walletService.getWalletById(id);
                    if (wallet != null)
                        System.out.println("✅ Wallet trouvé: " + wallet.getAddress() +
                                " | Solde: " + wallet.getBalance());
                    else
                        System.out.println("❌ Wallet introuvable !");
                    break;

                case 4:
                    System.out.print("Entrer l'ID du Wallet à supprimer: ");
                    String delId = scanner.nextLine();
                    walletService.deleteWallet(delId);
                    System.out.println("🗑 Wallet supprimé !");
                    break;

                case 0:
                    System.out.println("🛑 Sortie du menu Wallet");
                    break;

                default:
                    System.out.println("⚠️ Choix invalide !");
            }

        } while (choix != 0);

        scanner.close();
    }
}
