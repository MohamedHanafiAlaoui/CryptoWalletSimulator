package repositories.implementations;

import main.java.models.EthereumTransaction;
import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;
import main.java.repositories.RepositoryException;
import main.java.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Connection connection;

    public TransactionRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(EthereumTransaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions " +
                "(sender, receiver, hash, amount, fee, timestamp, status, priority, type, wallet_address, contract_address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, transaction.getSender());
            ps.setString(2, transaction.getReceiver());
            ps.setString(3, transaction.getHash());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBigDecimal(5, transaction.getFee());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getTimestamp()));
            ps.setString(7, transaction.getStatus().name());
            ps.setString(8, transaction.getPriority().name());
            ps.setString(9, transaction.getType().name());
            ps.setString(10, transaction.getWalletAddress());
            ps.setString(11, transaction.getContractAddress());

            ps.executeUpdate();
        }
    }

    @Override
    public List<EthereumTransaction> findAll() throws SQLException {
        List<EthereumTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EthereumTransaction tx = new EthereumTransaction(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("hash"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("fee"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        TransactionStatus.valueOf(rs.getString("status")),
                        FeePriority.valueOf(rs.getString("priority")),
                        CryptoType.valueOf(rs.getString("type")),
                        rs.getString("wallet_address"),
                        rs.getString("contract_address")
                );
                transactions.add(tx);
            }
        }
        return transactions;
    }

    @Override
    public EthereumTransaction findByHash(String hash) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE hash = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EthereumTransaction(
                            rs.getString("sender"),
                            rs.getString("receiver"),
                            rs.getString("hash"),
                            rs.getBigDecimal("amount"),
                            rs.getBigDecimal("fee"),
                            rs.getTimestamp("timestamp").toLocalDateTime(),
                            TransactionStatus.valueOf(rs.getString("status")),
                            FeePriority.valueOf(rs.getString("priority")),
                            CryptoType.valueOf(rs.getString("type")),
                            rs.getString("wallet_address"),
                            rs.getString("contract_address")
                    );
                }
            }
        }
        return null;
    }
}
