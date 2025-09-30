package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

public class BitcoinTransaction extends Transaction {
    private String sizeBytes;
    private String satoshiPerByte;

    public BitcoinTransaction(String id,
                              String sourceAddress,
                              String destinationAddress,
                              BigDecimal amount,
                              BigDecimal fees,
                              LocalDateTime createDate,
                              TransactionStatus status,
                              FeePriority priority,
                              CryptoType cryptoType,
                              String sizeBytes,
                              String satoshiPerByte) {
        super(id, sourceAddress, destinationAddress, amount, fees, createDate, status, priority, cryptoType);
        this.sizeBytes = sizeBytes;
        this.satoshiPerByte = satoshiPerByte;
    }

    @Override
    public BigDecimal calculateFees() {
        try {
            long size = Long.parseLong(sizeBytes);
            long satPerByte = Long.parseLong(satoshiPerByte);
            return BigDecimal.valueOf(size).multiply(BigDecimal.valueOf(satPerByte));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public int estimateConfirmationTime() {
        switch (getPriority()) {
            case FAST: return 10;
            case STANDARD: return 30;
            case ECONOMIC: return 60;
            default: return 30;
        }
    }

    // Getters & Setters
    public String getSizeBytes() { return sizeBytes; }
    public String getSatoshiPerByte() { return satoshiPerByte; }
    public void setSizeBytes(String sizeBytes) { this.sizeBytes = sizeBytes; }
    public void setSatoshiPerByte(String satoshiPerByte) { this.satoshiPerByte = satoshiPerByte; }
}
