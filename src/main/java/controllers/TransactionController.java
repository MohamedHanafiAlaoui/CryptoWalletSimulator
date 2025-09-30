package main.java.controllers;

import main.java.models.*;
import main.java.models.Enums.*;
import main.java.services.TransactionService;
import main.java.services.MempoolService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TransactionController {

    private final TransactionService transactionService;
    private final MempoolService mempoolService;
    private final Scanner scanner = new Scanner(System.in);

    public TransactionController(TransactionService transactionService, MempoolService mempoolService) {
        this.transactionService = transactionService;
        this.mempoolService = mempoolService;
    }

    public void createTransaction() {
        System.out.println("🔹 Choisir type de Transaction (1=Bitcoin, 2=Ethereum): ");
        int choice = safeReadInt();

        System.out.print("Entrer ID Transaction: ");
        String id = scanner.nextLine();

        System.out.print("Adresse Source: ");
        String source = scanner.nextLine();

        System.out.print("Adresse Destination: ");
        String destination = scanner.nextLine();

        BigDecimal amount = safeReadBigDecimal("Montant: ");

        System.out.print("Priority (1=FAST, 2=STANDARD, 3=ECONOMIC): ");
        int pChoice = safeReadInt();
        FeePriority priority = (pChoice == 1) ? FeePriority.FAST :
                               (pChoice == 2) ? FeePriority.STANDARD : FeePriority.ECONOMIC;

        Transaction tx;

        if (choice == 1) { // Bitcoin
            System.out.print("Size (bytes): ");
            String size = scanner.nextLine();

            System.out.print("Satoshi per byte: ");
            String satB = scanner.nextLine();

            tx = new BitcoinTransaction(
                    id, source, destination, amount, BigDecimal.ZERO,
                    LocalDateTime.now(), TransactionStatus.PENDING, priority,
                    CryptoType.BITCOIN, size, satB
            );
        } else { // Ethereum
            System.out.print("Gas Limit: ");
            String gasLimit = scanner.nextLine();

            System.out.print("Gas Price: ");
            String gasPrice = scanner.nextLine();

            tx = new EthereumTransaction(
                    id, source, destination, amount, BigDecimal.ZERO,
                    LocalDateTime.now(), TransactionStatus.PENDING, priority,
                    CryptoType.ETHEREUM, gasLimit, gasPrice
            );
        }

        tx.setFees(tx.calculateFees());
        mempoolService.addToMempool(tx);
        System.out.println("🕒 Transaction ajoutée au Mempool: " + tx.getId());
    }

    // تأكيد معاملة
    public void confirmTransaction() {
        System.out.print("Entrer ID Transaction à confirmer: ");
        String id = scanner.nextLine();

        Transaction tx = mempoolService.getTransactionById(id);
        if (tx != null) {
            tx.setStatus(TransactionStatus.CONFIRMED);
            if (transactionService != null) {
                transactionService.createTransaction(tx); // حفظ في قاعدة البيانات
            }
            mempoolService.removeFromMempool(id);
            System.out.println("✅ Transaction confirmée et enregistrée.");
        } else {
            System.out.println("❌ Transaction introuvable dans Mempool.");
        }
    }

    // عرض جميع المعاملات المؤكدة
    public void listConfirmedTransactions() {
        if (transactionService == null) {
            System.out.println("⚠️ TransactionService non disponible.");
            return;
        }

        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("⚠️ Aucun transaction confirmée.");
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

    private int safeReadInt() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("⚠️ Entrée invalide, veuillez entrer un nombre entier: ");
            }
        }
    }

    private BigDecimal safeReadBigDecimal(String prompt) {
        BigDecimal value = null;
        while (value == null) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                value = new BigDecimal(line);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrée invalide, veuillez entrer un nombre valide !");
            }
        }
        return value;
    }
}
