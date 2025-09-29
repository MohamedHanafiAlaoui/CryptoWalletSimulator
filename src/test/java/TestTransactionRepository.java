package test.java;

import main.java.models.*;
import main.java.models.Enums.*;
import main.java.repositories.implementations.TransactionRepositoryImpl;
import main.java.utils.DBConnection;
import main.java.validators.ConsoleColors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestTransactionRepository {

    public static void main(String[] args) {
        try {
            TransactionRepositoryImpl txRepo = new TransactionRepositoryImpl(() -> {
                try {
                    return DBConnection.getInstance().getConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Transaction btcTx = new BitcoinTransaction(
                    UUID.randomUUID().toString(),
                    "btc-src-123",
                    "btc-dest-456",
                    BigDecimal.valueOf(1.234),
                    BigDecimal.valueOf(0.01),
                    LocalDateTime.now(),
                    TransactionStatus.PENDING,
                    FeePriority.STANDARD,
                    CryptoType.BITCOIN,
                    "250",
                    "50"
            );
            txRepo.add(btcTx);
            System.out.println(ConsoleColors.GREEN + "Bitcoin Transaction ajouté: " + btcTx.getId() + ConsoleColors.RESET);

            Transaction ethTx = new EthereumTransaction(
                    UUID.randomUUID().toString(),
                    "eth-src-789",
                    "eth-dest-012",
                    BigDecimal.valueOf(5.678),
                    BigDecimal.valueOf(0.02),
                    LocalDateTime.now(),
                    TransactionStatus.PENDING,
                    FeePriority.FAST,
                    CryptoType.ETHEREUM,
                    "21000",
                    "100"
            );
            txRepo.add(ethTx);
            System.out.println(ConsoleColors.YELLOW + "Ethereum Transaction ajouté: " + ethTx.getId() + ConsoleColors.RESET);

            List<Transaction> transactions = txRepo.getAll();
            System.out.println(ConsoleColors.CYAN + "Toutes les transactions:" + ConsoleColors.RESET);
            transactions.forEach(t -> System.out.println(t.getId() + " | " + t.getSourceAddress() + " -> " + t.getDestinationAddress() + " | " + t.getCryptoType()));

            btcTx.setAmount(BigDecimal.valueOf(2.5));
            btcTx.setStatus(TransactionStatus.CONFIRMED);
            txRepo.update(btcTx);
            System.out.println(ConsoleColors.GREEN + "Bitcoin Transaction mis à jour: " + btcTx.getAmount() + " | " + btcTx.getStatus() + ConsoleColors.RESET);

            txRepo.delete(ethTx.getId());
            System.out.println(ConsoleColors.RED + "Ethereum Transaction supprimé: " + ethTx.getId() + ConsoleColors.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
