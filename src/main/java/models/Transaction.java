package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

abstract public class Transaction {

    private String id;
    private String sourceAddress;
    private String destinationAddress; // ✅ تمت الإضافة
    private BigDecimal amount;
    private BigDecimal fees;
    private LocalDateTime createDate;
    private TransactionStatus status;
    private FeePriority priority;
    private CryptoType cryptoType;

    public Transaction(String id, String sourceAddress, String destinationAddress, // ✅
                       BigDecimal amount, BigDecimal fees, LocalDateTime createDate,
                       TransactionStatus status, FeePriority priority, CryptoType cryptoType) {
        this.id = id;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress; // ✅
        this.amount = amount;
        this.fees = fees;
        this.createDate = createDate;
        this.status = status;
        this.priority = priority;
        this.cryptoType = cryptoType;
    }

    public String getId() {
        return id;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {  // ✅ getter
        return destinationAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public FeePriority getPriority() {
        return priority;
    }

    public CryptoType getCryptoType() {
        return cryptoType;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    abstract public BigDecimal calculateFees();

    public abstract int estimateConfirmationTime();

    public void updateStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public void setPriority(FeePriority priority) {
        this.priority = priority;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    
}
