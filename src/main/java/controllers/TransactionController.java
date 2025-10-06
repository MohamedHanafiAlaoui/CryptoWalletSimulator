package main.java.controllers;

import main.java.models.*;
import main.java.models.Enums.*;
import main.java.services.TransactionService;
import main.java.services.MempoolService;
import main.java.services.WalletService;
import main.java.services.implementations.TransactionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class TransactionController {

    private final TransactionService transactionService;
    private final MempoolService mempoolService;
    private final WalletService walletService;
    private final Scanner scanner = new Scanner(System.in);

    public TransactionController(TransactionService transactionService, MempoolService mempoolService,
            WalletService walletService) {
        this.transactionService = transactionService;
        this.mempoolService = mempoolService;
        this.walletService = walletService;
    }

    public void createTransaction() {
        System.out.println(" Choisir type de Transaction (1=Bitcoin, 2=Ethereum): ");
        int choice = safeReadInt();

        String id = UUID.randomUUID().toString();

        System.out.print("Adresse Source: ");
        String sourceAddress = scanner.nextLine();

        System.out.print("Adresse Destination: ");
        String destAddress = scanner.nextLine();

        BigDecimal amount = safeReadBigDecimal("Montant: ");

        System.out.print("Priority (1=FAST, 2=STANDARD, 3=ECONOMIC): ");
        int pChoice = safeReadInt();
        FeePriority priority = (pChoice == 1) ? FeePriority.FAST
                : (pChoice == 2) ? FeePriority.STANDARD : FeePriority.ECONOMIC;

        Transaction tx;

        if (choice == 1) {
            System.out.print("Size (bytes): ");
            String size = scanner.nextLine();

            System.out.print("Satoshi per byte: ");
            String satB = scanner.nextLine();

            tx = new BitcoinTransaction(
                    id, sourceAddress, destAddress, amount, BigDecimal.ZERO,
                    LocalDateTime.now(), TransactionStatus.PENDING, priority,
                    CryptoType.BITCOIN, size, satB);
        } else {
            System.out.print("Gas Limit: ");
            String gasLimit = scanner.nextLine();

            System.out.print("Gas Price: ");
            String gasPrice = scanner.nextLine();

            tx = new EthereumTransaction(
                    id, sourceAddress, destAddress, amount, BigDecimal.ZERO,
                    LocalDateTime.now(), TransactionStatus.PENDING, priority,
                    CryptoType.ETHEREUM, gasLimit, gasPrice);
        }

        tx.setFees(tx.calculateFees());
        mempoolService.addToMempool(tx);
        System.out.println(" Transaction ajoutée au Mempool: " + tx.getId());
    }

    public void confirmTransaction() {
        System.out.print("Entrer ID Transaction à confirmer: ");
        String id = scanner.nextLine();

        Transaction tx = mempoolService.getTransactionById(id);
        if (tx != null) {
            Wallet sender = walletService.getWalletByAddress(tx.getSourceAddress());
            Wallet receiver = walletService.getWalletByAddress(tx.getDestinationAddress());

            if (sender == null || receiver == null) {
                System.out.println(" Wallet source ou destination introuvable !");
                return;
            }

            walletService.sendTransaction(sender, receiver, tx.getAmount(), tx);

            tx.setStatus(TransactionStatus.CONFIRMED);

            mempoolService.removeFromMempool(id);
            System.out.println(" Transaction confirmée et solde mis à jour.");
        } else {
            System.out.println(" Transaction introuvable dans Mempool.");
        }
    }

    public void listConfirmedTransactions() {
        if (transactionService == null) {
            System.out.println(" TransactionService non disponible.");
            return;
        }

        List<Transaction> transactions = transactionService.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println(" Aucun transaction confirmée.");
        } else {
            for (Transaction tx : transactions) {
                System.out.println("➡ ID: " + tx.getId() +
                        " | Type: " + tx.getCryptoType() +
                        " | Amount: " + tx.getAmount() +
                        " | Fees: " + tx.getFees() +
                        " | Status: " + tx.getStatus());
            }
        }
    }


    // public long sumTransactins()
    // {
    //     List<Transaction> transactions = transactionService.getAllTransactions();

    //     return (transactions.stream()
    //                 .filter(x -> x.getStatus().equals("Pending") )
    //                 .count());

        
                    
    // }

    // public void AficheTransactins()
    // {
    //     List<Transaction> transactions = transactionService.getAllTransactions();

    //     (transactions.stream()
    //                 .filter((x , a )-> x.getCryptoType().equals("Bitcoins") && a.g.equals("Pending") )
    //                 .count());

        
                    
    // }




    private int safeReadInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print(" Entrée invalide, veuillez entrer un nombre entier: ");
            }
        }
    }

    private BigDecimal safeReadBigDecimal(String prompt) {
        BigDecimal value = null;
        while (value == null) {
            System.out.print(prompt);
            try {
                value = new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(" Entrée invalide, veuillez entrer un nombre valide !");
            }
        }
        return value;
    }
}
