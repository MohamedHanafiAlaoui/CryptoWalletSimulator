package main.java.controllers;

import main.java.models.Transaction;
import main.java.services.MempoolService;

import java.util.List;
import java.util.Scanner;

public class MempoolController {

    private final MempoolService mempoolService;
    private final Scanner scanner = new Scanner(System.in);

    public MempoolController(MempoolService mempoolService) {
        this.mempoolService = mempoolService;
    }

    public void listMempoolTransactions() {
        List<Transaction> transactions = mempoolService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println(" Mempool .");
        } else {
            for (Transaction tx : transactions) {
                System.out.println("➡ ID: " + tx.getId() +
                        " | From: " + tx.getSourceAddress() +
                        " → To: " + tx.getDestinationAddress() +
                        " | Amount: " + tx.getAmount() +
                        " | Fees: " + tx.getFees() +
                        " | Status: " + tx.getStatus());
            }
        }
    }

    public void getTransactionById() {
        System.out.print("Entrer ID Transaction: ");
        String id = scanner.nextLine();

        Transaction tx = mempoolService.getTransactionById(id);
        if (tx != null) {
            System.out.println(" Transaction trouvée dans Mempool: " +
                    tx.getCryptoType() +
                    " | From: " + tx.getSourceAddress() +
                    " → To: " + tx.getDestinationAddress() +
                    " | Amount: " + tx.getAmount() +
                    " | Fees: " + tx.getFees() +
                    " | Status: " + tx.getStatus());
        } else {
            System.out.println(" Transaction introuvable dans Mempool.");
        }
    }

    public void removeTransaction() {
        System.out.print("Entrer ID Transaction à supprimer du Mempool: ");
        String id = scanner.nextLine();

        mempoolService.removeFromMempool(id);
        System.out.println(" Transaction supprimée du Mempool.");
    }
}
