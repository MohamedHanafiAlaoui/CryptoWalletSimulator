package main.java.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import main.java.models.Enums.CryptoType;
import main.java.models.Enums.FeePriority;
import main.java.models.Enums.TransactionStatus;

public class Transaction {

    private String id;
    private String sourceAddress;
    private   BigDecimal amount ;
    private BigDecimal fees;
    private LocalDateTime createDate;
    private  TransactionStatus status ;
    private  FeePriority priority ;
    private CryptoType  cryptoType ;

    
}
