package main.java.repositories.implementations;

import main.java.models.BitcoinWallet;
import main.java.models.EthereumWallet;
import main.java.models.Wallet;
import main.java.models.Enums.CryptoType;
import main.java.repositories.WalletRepository;
import main.java.repositories.RepositoryException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import main.java.validators.ConsoleColors;

public class WalletRepositoryImpl implements WalletRepository {

    private final Supplier<Connection> connectionSupplier;

    public WalletRepositoryImpl(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    private Connection getConnection() throws SQLException {
        return connectionSupplier.get();
    }

    private Wallet mapRowToWallet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String address = rs.getString("address");
        BigDecimal balance = rs.getBigDecimal("balance");
        Timestamp created = rs.getTimestamp("created_at");
        String cryptoTypeStr = rs.getString("crypto_type");

        LocalDateTime createDate = created != null ? created.toLocalDateTime() : LocalDateTime.now();

        if ("BITCOIN".equalsIgnoreCase(cryptoTypeStr)) {
            return new BitcoinWallet(
                    id,
                    address,
                    CryptoType.BITCOIN,
                    balance != null ? balance : BigDecimal.ZERO,
                    createDate,
                    "1.0",
                    "mainnet"
            );
        } else if ("ETHEREUM".equalsIgnoreCase(cryptoTypeStr)) {
            return new EthereumWallet(
                    id,
                    address,
                    CryptoType.ETHEREUM,
                    balance != null ? balance : BigDecimal.ZERO,
                    createDate,
                    1
            );
        } else {
            throw new RepositoryException("❌ Unknown crypto type: " + cryptoTypeStr);
        }
    }

    @Override
    public void add(Wallet wallet) {
        String sql = "INSERT INTO wallets (id, address, balance, crypto_type, created_at, version, network, chain_id) "
                + "VALUES (?, ?, ?, ?::crypto_type, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wallet.getId());
            ps.setString(2, wallet.getAddress());
            ps.setBigDecimal(3, wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO);
            ps.setString(4, wallet.getCryptoType().name());
            ps.setTimestamp(5, Timestamp.valueOf(wallet.getCreatDate()));

            if (wallet instanceof BitcoinWallet) {
                BitcoinWallet btc = (BitcoinWallet) wallet;
                ps.setString(6, btc.getVersion());
                ps.setString(7, btc.getNetwork());
                ps.setNull(8, Types.INTEGER);
            } else if (wallet instanceof EthereumWallet) {
                EthereumWallet eth = (EthereumWallet) wallet;
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setInt(8, eth.getChainId());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
                
        System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

            throw new RepositoryException("❌ Error inserting Wallet", e);
        }
    }

    @Override
    public Wallet getById(String id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToWallet(rs);
                }
                return null;
            }
        } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

            throw new RepositoryException("❌ Error fetching Wallet by ID", e);
        }
    }

    @Override
    public Wallet getByAddress(String address) {
        String sql = "SELECT * FROM wallets WHERE address = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToWallet(rs);
                }
                return null;
            }
        } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

            throw new RepositoryException("❌ Error fetching Wallet by address", e);
        }
    }

    @Override
    public List<Wallet> getAll() {
        String sql = "SELECT * FROM wallets";
        List<Wallet> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToWallet(rs));
            }
            return list;
        } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

            throw new RepositoryException("❌ Error fetching all Wallets", e);
        }
    }

    @Override
public void update(Wallet wallet) {
    String sql = "UPDATE wallets SET address = ?, balance = ?, crypto_type = ?::crypto_type, updated_at = ?, version = ?, network = ?, chain_id = ? WHERE id = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, wallet.getAddress());
        ps.setBigDecimal(2, wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO);

        // ENUM crypto_type
        ps.setObject(3, wallet.getCryptoType().name(), Types.OTHER);

        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

        if (wallet instanceof BitcoinWallet) {
            BitcoinWallet btc = (BitcoinWallet) wallet;
            ps.setString(5, btc.getVersion());
            ps.setString(6, btc.getNetwork());
            ps.setNull(7, Types.INTEGER);
        } else if (wallet instanceof EthereumWallet) {
            EthereumWallet eth = (EthereumWallet) wallet;
            ps.setNull(5, Types.VARCHAR);
            ps.setNull(6, Types.VARCHAR);
            ps.setInt(7, eth.getChainId());
        } else {
            ps.setNull(5, Types.VARCHAR);
            ps.setNull(6, Types.VARCHAR);
            ps.setNull(7, Types.INTEGER);
        }

        ps.setString(8, wallet.getId());

        int rows = ps.executeUpdate();
        if (rows == 0) {
            throw new RepositoryException("❌ No Wallet found with ID: " + wallet.getId());
        }
    } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

        throw new RepositoryException("❌ Error updating Wallet", e);

    }
}

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM wallets WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
                System.err.println(ConsoleColors.RED + " SQL Error: " + ConsoleColors.RESET+ e.getMessage() );

            throw new RepositoryException("❌ Error deleting Wallet", e);
        }
    }
}
