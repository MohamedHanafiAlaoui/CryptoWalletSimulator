package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

public class EthereumTransaction extends Transaction {
    private String gasLimit;
    private String gasPrice;

    public EthereumTransaction(String id, 
                               String sourceAddress, 
                               String destinationAddress, 
                               BigDecimal amount, 
                               BigDecimal fees,
                               LocalDateTime createDate, 
                               TransactionStatus status, 
                               FeePriority priority, 
                               CryptoType cryptoType,
                               String gasLimit, 
                               String gasPrice) {
        super(id, sourceAddress, destinationAddress, amount, fees, createDate, status, priority, cryptoType); 
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
    }

    @Override
    public BigDecimal calculateFees() {
        try {
            long gasLimitLong = Long.parseLong(gasLimit);
            long gasPriceLong = Long.parseLong(gasPrice);
            return BigDecimal.valueOf(gasLimitLong).multiply(BigDecimal.valueOf(gasPriceLong));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public int estimateConfirmationTime() {
        switch (getPriority()) {
            case FAST:
                return 5;
            case STANDARD:
                return 15;
            case ECONOMIC:
                return 30;
            default:
                return 20;
        }
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }
}
