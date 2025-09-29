package main.java.repositories.implementations;

import main.java.models.*;
import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;
import main.java.repositories.RepositoryException;
import main.java.repositories.TransactionRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.math.BigDecimal;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final Supplier<Connection> connectionSupplier;

    public TransactionRepositoryImpl(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    private Connection getConnection() throws SQLException {
        return connectionSupplier.get();
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String source = rs.getString("source_address");
        String destination = rs.getString("destination_address");
        BigDecimal amount = rs.getBigDecimal("amount");
        BigDecimal fees = rs.getBigDecimal("fees");
        LocalDateTime created = rs.getTimestamp("created_at").toLocalDateTime();
        String cryptoTypeStr = rs.getString("crypto_type");
        TransactionStatus status = TransactionStatus.valueOf(rs.getString("status"));
        FeePriority priority = FeePriority.valueOf(rs.getString("priority"));

        if ("BITCOIN".equalsIgnoreCase(cryptoTypeStr)) {
            String sizeBytes = rs.getString("size_bytes");
            String satoshiPerByte = rs.getString("satoshi_per_byte");
            return new BitcoinTransaction(id, source, destination, amount, fees, created, status, priority,
                    CryptoType.BITCOIN, sizeBytes, satoshiPerByte);
        } else if ("ETHEREUM".equalsIgnoreCase(cryptoTypeStr)) {
            String gasLimit = rs.getString("gas_limit");
            String gasPrice = rs.getString("gas_price");
            return new EthereumTransaction(id, source, destination, amount, fees, created, status, priority,
                    CryptoType.ETHEREUM, gasLimit, gasPrice);
        } else {
            throw new RepositoryException("❌ Type de transaction inconnu : " + cryptoTypeStr);
        }
    }

    @Override
    public void add(Transaction transaction) {
        String sql = "INSERT INTO transactions (id, source_address, destination_address, amount, fees, created_at, status, priority, crypto_type, size_bytes, satoshi_per_byte, gas_limit, gas_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, transaction.getId());
            ps.setString(2, transaction.getSourceAddress());
            ps.setString(3, transaction.getDestinationAddress());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setBigDecimal(5, transaction.getFees());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getCreateDate()));
            ps.setObject(7, transaction.getStatus(), java.sql.Types.OTHER); // <-- تم التعديل
            ps.setObject(8, transaction.getPriority(), java.sql.Types.OTHER); // <-- تم التعديل
            ps.setObject(9, transaction.getCryptoType(), java.sql.Types.OTHER);

            if (transaction instanceof BitcoinTransaction) {
                BitcoinTransaction btc = (BitcoinTransaction) transaction;
                ps.setString(10, btc.getSizeBytes());
                ps.setString(11, btc.getSatoshiPerByte());
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.VARCHAR);
            } else if (transaction instanceof EthereumTransaction) {
                EthereumTransaction eth = (EthereumTransaction) transaction;
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
                ps.setString(12, eth.getGasLimit());
                ps.setString(13, eth.getGasPrice());
            } else {
                throw new RepositoryException("❌ Type de transaction non supporté");
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de l'ajout de la transaction", e);
        }
    }

    @Override
    public Transaction getById(String id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowToTransaction(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la récupération de la transaction par ID", e);
        }
    }

    @Override
    public List<Transaction> getBySource(String address) {
        String sql = "SELECT * FROM transactions WHERE source_address = ?";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRowToTransaction(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la récupération des transactions par adresse source", e);
        }
    }

    @Override
    public List<Transaction> getByDestination(String address) {
        String sql = "SELECT * FROM transactions WHERE destination_address = ?";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRowToTransaction(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la récupération des transactions par adresse destination", e);
        }
    }

    @Override
    public List<Transaction> getAll() {
        String sql = "SELECT * FROM transactions";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                list.add(mapRowToTransaction(rs));
            return list;
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la récupération de toutes les transactions", e);
        }
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE transactions SET amount = ?, fees = ?, status = ?, priority = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, transaction.getAmount());
            ps.setBigDecimal(2, transaction.getFees());
            ps.setObject(3, transaction.getStatus(), java.sql.Types.OTHER); // تعديل هنا
            ps.setObject(4, transaction.getPriority(), java.sql.Types.OTHER);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, transaction.getId());

            int rows = ps.executeUpdate();
            if (rows == 0)
                throw new RepositoryException("❌ Transaction introuvable pour mise à jour ID: " + transaction.getId());
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la mise à jour de la transaction", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("❌ Échec de la suppression de la transaction", e);
        }
    }
}
