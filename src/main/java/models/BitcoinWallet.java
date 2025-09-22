package main.java.models;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import main.java.models.Wallet;
import main.java.models.Enums.CryptoType;


public class BitcoinWallet extends Wallet {
    
    private String version ;
    private String network ;

    public BitcoinWallet(String id, String address, CryptoType cryptoType, BigDecimal balance, LocalDateTime creatDate , String version, String network) {

        super(id, address, cryptoType, balance, creatDate);
        this.version = version;
        this.network = network;
    }

    public String getVersion() {
        return version;
    }

    public String getNetwork() {
        return network;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public String generateAddress() {
        return ("btc-" + System.currentTimeMillis());
    }

    @Override
    public  boolean validateAddress(String address)
    {
        return address != null && (address.startsWith("1") || address.startsWith("3") || address.startsWith("bc1"));
    }


    
}
