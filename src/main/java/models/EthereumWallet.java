package main.java.models;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import main.java.models.Wallet;
import main.java.models.Enums.CryptoType;

public class EthereumWallet extends Wallet {
    private int chainId;

    public EthereumWallet(String id, String address, CryptoType cryptoType, BigDecimal balance, LocalDateTime creatDate, int chainId) {
        super(id, address, cryptoType, balance, creatDate);
        this.chainId = chainId;
    }



    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }
    @Override
    public  String generateAddress()
    {
        StringBuilder  sb = new StringBuilder("0x") ;

        String hexChars = "0123456789abcdef";
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 40; i++) {
            int index = random.nextInt(16);
            sb.append(hexChars.charAt(index));
        }
        return sb.toString() ;
    };
    @Override
    public  boolean validateAddress(String address)
    {
        return address != null && address.startsWith("0x") && address.length() == 42;
    };

    
}
