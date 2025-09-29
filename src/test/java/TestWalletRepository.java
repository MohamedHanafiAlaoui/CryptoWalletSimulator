package test.java;

import main.java.models.BitcoinWallet;
import main.java.models.EthereumWallet;
import main.java.models.Enums.CryptoType;
import main.java.models.Wallet;
import main.java.repositories.implementations.WalletRepositoryImpl;
import main.java.utils.DBConnection;
import main.java.validators.ConsoleColors;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestWalletRepository {

    public static void main(String[] args) {
        try {
            WalletRepositoryImpl walletRepo = new WalletRepositoryImpl(() -> {
                try {
                    return DBConnection.getInstance().getConnection();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Wallet btcWallet = new BitcoinWallet(
                    UUID.randomUUID().toString(),
                    "btc-123458",
                    CryptoType.BITCOIN,
                    BigDecimal.valueOf(1.234),
                    LocalDateTime.now(),
                    "1.0",
                    "mainnet"
            );
            walletRepo.add(btcWallet);
            System.out.println("Bitcoin Wallet ajouté: " + btcWallet.getAddress());

            Wallet ethWallet = new EthereumWallet(
                    UUID.randomUUID().toString(),
                    "0xabcdef1234567890abcdef1234567890abcdef18",
                    CryptoType.ETHEREUM,
                    BigDecimal.valueOf(5.678),
                    LocalDateTime.now(),
                    1
            );
            walletRepo.add(ethWallet);
            System.out.println(ConsoleColors.YELLOW + "Ethereum Wallet ajouté: " + ethWallet.getAddress() + ConsoleColors.RESET);

            List<Wallet> wallets = walletRepo.getAll();
            System.out.println(ConsoleColors.GREEN +"Tous les wallets:" + ConsoleColors.RESET);
            wallets.forEach(w -> System.out.println(w.getId() + " | " + w.getAddress() + " | " + w.getCryptoType()));

            btcWallet.setBalance(BigDecimal.valueOf(2.5));
            walletRepo.update(btcWallet);
            System.out.println(ConsoleColors.GREEN +"Bitcoin Wallet mis à jour: " + btcWallet.getBalance() + ConsoleColors.RESET);

            walletRepo.delete(ethWallet.getId());
            System.out.println( ConsoleColors.RED + "Ethereum Wallet supprimé: " + ethWallet.getId() + ConsoleColors.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
