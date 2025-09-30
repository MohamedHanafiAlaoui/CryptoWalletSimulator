package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

public class EthereumTransaction extends Transaction {
    private BigDecimal gasLimit;
    private BigDecimal gasPrice;

    public EthereumTransaction(String id,
                               String sourceAddress,
                               String destinationAddress,
                               BigDecimal amount,
                               BigDecimal fees,
                               LocalDateTime createDate,
                               TransactionStatus status,
                               FeePriority priority,
                               CryptoType cryptoType,
                               BigDecimal gasLimit,
                               BigDecimal gasPrice) {
        super(id, sourceAddress, destinationAddress, amount, fees, createDate, status, priority, cryptoType);
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
    }

    @Override
    public BigDecimal calculateFees() {
        if (gasLimit != null && gasPrice != null) {
            return gasLimit.multiply(gasPrice);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int estimateConfirmationTime() {
        switch (getPriority()) {
            case FAST: return 15;
            case STANDARD: return 60;
            case ECONOMIC: return 180;
            default: return 60;
        }
    }

    // Getters & Setters
    public BigDecimal getGasLimit() { return gasLimit; }
    public BigDecimal getGasPrice() { return gasPrice; }
    public void setGasLimit(BigDecimal gasLimit) { this.gasLimit = gasLimit; }
    public void setGasPrice(BigDecimal gasPrice) { this.gasPrice = gasPrice; }
}
